variable "aws_region" {
  default = "us-west-1"
  description = "aws region"
}

variable "cluster_name" {
  default     = "url-shortener-cluster"
  description = "cluster name"
}

variable "vpc_name" {
  default     = "url-shortener-eks-vpc"
  description = "vpc name"
}

variable "kubernetes_version" {
  default     = 1.27
  description = "kubernetes version"
}

variable "vpc_cidr" {
  default     = "10.0.0.0/16"
  description = "default CIDR range of the VPC"
}

variable "ami_type" {
  default     = "AL2_x86_64"
  description = "AMI type for the EKS worker nodes"
}

variable "instance_types" {
  default     = ["t3.medium"]
  description = "instance types for the EKS worker nodes"
}

variable "node_group_min_size" {
  default     = 2
  description = "minimum number of nodes in the EKS node group"
}

variable "node_group_max_size" {
  default     = 6
  description = "maximum number of nodes in the EKS node group"
}

variable "node_group_desired_size" {
  default     = 2
  description = "desired number of nodes in the EKS node group"
}
