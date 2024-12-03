resource "aws_db_instance" "url_shortener_postgresql" {
  identifier           = "url-shortener"
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "postgres"
  engine_version       = "13.16"
  instance_class       = "db.t3.micro"
  db_name              = "urlShortenerDb"
  username             = "postgres"
  password             = "postgres"
  parameter_group_name = "default.postgres13"

  vpc_security_group_ids = [aws_security_group.all_worker_mgmt.id]

  db_subnet_group_name = aws_db_subnet_group.url_shortener_subnet_group.name

  multi_az             = true
  publicly_accessible  = true
  backup_retention_period = 7

  final_snapshot_identifier = "url-shortener-db-final-snapshot"

  tags = {
    Name = "UrlShortenerRDS"
  }

  skip_final_snapshot = true
}

output "rds_endpoint" {
  value = aws_db_instance.url_shortener_postgresql.endpoint
}

resource "aws_db_subnet_group" "url_shortener_subnet_group" {
  name        = "url-shortener-subnet-group"
  description = "Subnets for the URL shortener RDS"

  subnet_ids = module.vpc.public_subnets

  tags = {
    Name = "url-shortener-db-subnet-group"
  }
}

resource "aws_security_group_rule" "rds_ingress_from_eks" {
  description       = "Allow EKS to access PostgreSQL RDS"
  security_group_id = aws_security_group.all_worker_mgmt.id  # RDS Security Group
  from_port         = 5432
  to_port           = 5432
  protocol          = "tcp"
  source_security_group_id = aws_security_group.all_worker_mgmt.id  # EKS Security Group
  type              = "ingress"
}

