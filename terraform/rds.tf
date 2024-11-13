resource "aws_db_instance" "url_shortener_postgresql" {
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

  skip_final_snapshot = true  # Pular o snapshot final quando a instância for destruída
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
