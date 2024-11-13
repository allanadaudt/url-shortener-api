# URL Shortener API
The URL Shortener API allows users to create shortened versions of long URLs, making them easier to share and manage.
With a simple endpoint, users can input a lengthy URL and receive a unique, shortened link in return. 

## How to run

### Locally
To run the application locally using Docker Compose, follow these steps:

1 - First, ensure you have Docker and Docker Compose installed on your system.

2- Clone the repository and navigate to the project directory.

3- Run the following command to start the application with Docker Compose:
>./scripts/run-local

After a successful start, the application will be accessible at http://localhost:8080.

## API Endpoints

### Create Shortened URL

- Endpoint: *POST* http://localhost:8080/shorten
- Description: Create a shortened URL for the provided original URL.

> **Request Body Example**:
```json
  {
    "originalUrl": "https://www.example.com",
    "urlIdentifier": "example"
  }
```
> **Response Example**: Status Code: 200 OK
```json
{
"shortenedUrl": "http://short.url/example"
}
```

### List All Shortened URLs

- Endpoint: *GET* http://localhost:8080/shorten
- Description: Retrieve a list of all shortened URLs.

> **Response Example**: Status Code: 200 OK
```json
[
  {
    "shortened_url": "http://short.url/example_one"
  },
  {
    "shortened_url": "http://short.url/example_two"
  }
]
```

### Get Original URL from Shortened URL

- Endpoint: *GET* http://localhost:8080/shorten/convert/{id}
- Description: Retrieve the original URL based on the shortened URL identifier.

> **Request Path Example**:
```
  http://localhost:8080/shorten/convert/example
```

> **Response Example**: Status Code 200 OK  
```
https://www.example.com
```

*404 Not Found if the shortened URL doesn't exist.*

### Delete Shortened URL

- Endpoint: *DELETE* http://localhost:8080/shorten/{id}
- Description: Delete a shortened URL by its identifier.

> **Request Path Example**:
```
  http://localhost:8080/shorten/example
```
> **Response**: Status Code 204 No Content

## Provisioning and Deployment on Kubernetes with Terraform and Manifests

### Install AWS CLI
As the first step, you need to install AWS CLI as we will use the AWS CLI (`aws configure`) command to connect Terraform with AWS in the next steps.

Follow the below link to Install AWS CLI.
```
https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html
```

### Install Terraform
Next, Install Terraform using the below link.
```
https://developer.hashicorp.com/terraform/tutorials/aws-get-started/install-cli
```

### Connect Terraform with AWS
Run `aws configure` command and provide the AWS Security credentials.

### Initialize Terraform
Run `terraform init` in the directory containing the Terraform files. This will initialize the terraform environment for you and download the modules, providers and other configuration required.

### Optionally review the terraform configuration
Run `terraform plan` to see the configuration it creates when executed.

### Apply to terraform configuration to create EKS cluster with VPC
Run `terraform apply`.

### Obtain the Kubeconfig for the EKS Cluster
Run the following command to update your kubeconfig with the cluster created by Terraform:

```
aws eks --region <your-region> update-kubeconfig --name <your-cluster-name>
```

### Verify Connection to the Cluster
Confirm that the context is pointing to the correct cluster with command:

```
kubectl config current-context
```

Then, check the connection by running:

```
kubectl get nodes
```

### Apply the Kubernetes Manifests
Navigate to the directory where your Kubernetes manifests are stored, then run:

```
kubectl apply -f .
```

This command will apply all Kubernetes manifests in the current directory to the cluster, including deployments, services and ingresses.

>After completing these steps, your application should be running in the cluster.