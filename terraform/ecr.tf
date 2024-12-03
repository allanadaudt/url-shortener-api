resource "aws_ecr_repository" "url_shortener_repo" {
  name = "url-shortener-api"
}

data "aws_ecr_authorization_token" "auth_token" {
  registry_id = "381492214831"
}

resource "null_resource" "docker_push" {
  depends_on = [aws_ecr_repository.url_shortener_repo]

  provisioner "local-exec" {
    command = <<EOT
      # Realizando o build da imagem Docker
      docker build -t url-shortener-api:latest .

      # Realizando o login no ECR
      aws ecr get-login-password --region us-west-1 | docker login --username AWS --password-stdin 381492214831.dkr.ecr.us-west-1.amazonaws.com

      # Tagging da imagem com o repositório ECR
      docker tag url-shortener-api:latest 381492214831.dkr.ecr.us-west-1.amazonaws.com/url-shortener-api:latest

      # Enviando a imagem para o repositório ECR
      docker push 381492214831.dkr.ecr.us-west-1.amazonaws.com/url-shortener-api:latest
    EOT
  }
}
