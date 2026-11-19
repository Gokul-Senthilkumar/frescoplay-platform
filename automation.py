#!/usr/bin/env python3
import json
import shutil
import subprocess
from pathlib import Path

import boto3

BASE_DIR = Path(__file__).resolve().parent
CREDENTIALS_FILE = Path("/etc/labapi.json")
REGION = "us-east-1"
CLUSTER_NAME = "eks-cicd"
DB_INSTANCE_IDENTIFIER = "frescoplay-mysql-prod"
K8S_DIR = BASE_DIR / "k8s" / "base"
RENDER_DIR = BASE_DIR / ".rendered-k8s"
RENDER_FILE = RENDER_DIR / "manifests.yaml"


def load_session():
    if not CREDENTIALS_FILE.is_file():
        raise RuntimeError(f"Credentials file not found: {CREDENTIALS_FILE}")
    with CREDENTIALS_FILE.open("r", encoding="utf-8") as file:
        credentials = json.load(file)
    api = credentials["api_access_details"]
    return boto3.Session(
        aws_access_key_id=api["accessid"],
        aws_secret_access_key=api["accesskey"],
        region_name=REGION
    )


def get_account_id(session):
    account_id = session.client("sts").get_caller_identity()["Account"]
    if not account_id or not account_id.isdigit() or len(account_id) != 12:
        raise RuntimeError(f"Invalid AWS account ID: {account_id}")
    return account_id


def get_rds_endpoint(session):
    response = session.client("rds", region_name=REGION).describe_db_instances(
        DBInstanceIdentifier=DB_INSTANCE_IDENTIFIER
    )
    instances = response.get("DBInstances", [])
    if not instances:
        raise RuntimeError(f"RDS instance not found: {DB_INSTANCE_IDENTIFIER}")
    endpoint = instances[0].get("Endpoint", {}).get("Address")
    if not endpoint:
        raise RuntimeError("RDS endpoint is not available yet.")
    return endpoint


def validate_source_files():
    required_files = list(K8S_DIR.rglob("*.yaml"))
    if not required_files:
        raise RuntimeError(f"No Kubernetes YAML files found in {K8S_DIR}")
    combined = "\n".join(
        path.read_text(encoding="utf-8") for path in required_files
    )
    required_placeholders = [
        "${AWS_ACCOUNT_ID}",
        "${RDS_ENDPOINT}",
        "${IMAGE_TAG}"
    ]
    for placeholder in required_placeholders:
        if placeholder not in combined:
            raise RuntimeError(
                f"Missing required placeholder in Kubernetes files: {placeholder}"
            )


def render_manifests(account_id, rds_endpoint, image_tag):
    if not shutil.which("kubectl"):
        raise RuntimeError("kubectl is not installed.")
    RENDER_DIR.mkdir(exist_ok=True)
    rendered = subprocess.run(
        ["kubectl", "kustomize", str(BASE_DIR / "k8s" / "overlays" / "lab")],
        capture_output=True,
        text=True,
        check=True
    ).stdout
    rendered = rendered.replace("${AWS_ACCOUNT_ID}", account_id)
    rendered = rendered.replace("${RDS_ENDPOINT}", rds_endpoint)
    rendered = rendered.replace("${IMAGE_TAG}", image_tag)
    RENDER_FILE.write_text(rendered, encoding="utf-8")


def validate_rendered_file(account_id, rds_endpoint, image_tag):
    rendered = RENDER_FILE.read_text(encoding="utf-8")
    if "${AWS_ACCOUNT_ID}" in rendered:
        raise RuntimeError("AWS account placeholder was not replaced.")
    if "${RDS_ENDPOINT}" in rendered:
        raise RuntimeError("RDS endpoint placeholder was not replaced.")
    if "${IMAGE_TAG}" in rendered:
        raise RuntimeError("Image tag placeholder was not replaced.")
    if account_id not in rendered:
        raise RuntimeError("Rendered file does not contain the AWS account ID.")
    if rds_endpoint not in rendered:
        raise RuntimeError("Rendered file does not contain the RDS endpoint.")
    if image_tag not in rendered:
        raise RuntimeError("Rendered file does not contain the image tag.")


def main():
    print("FrescoPlay local render automation started")
    session = load_session()
    account_id = get_account_id(session)
    rds_endpoint = get_rds_endpoint(session)
    image_tag = "local-test"
    print(f"AWS account ID: {account_id}")
    print(f"RDS endpoint: {rds_endpoint}")
    print(f"Local image tag: {image_tag}")
    validate_source_files()
    render_manifests(account_id, rds_endpoint, image_tag)
    validate_rendered_file(account_id, rds_endpoint, image_tag)
    print(f"Rendered Kubernetes manifests: {RENDER_FILE}")
    print("Source Kubernetes files and GitHub workflow were not modified.")
    print("This rendered file is for local testing only.")


if __name__ == "__main__":
    main()