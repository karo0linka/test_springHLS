#!/bin/bash

STACK_NAME="springboot-ecs-alb"
TEMPLATE_FILE="springboot-ecs-alb.yaml"
REGION="us-east-1"

echo " Desplegando stack: $STACK_NAME"

# Despliegue del stack
aws cloudformation deploy \
  --template-file $TEMPLATE_FILE \
  --stack-name $STACK_NAME \
  --capabilities CAPABILITY_NAMED_IAM \
  --region $REGION

if [ $? -ne 0 ]; then
  echo " Error al desplegar el stack."
  exit 1
fi

echo " Esperando a que el stack se complete..."
aws cloudformation wait stack-create-complete \
  --stack-name $STACK_NAME \
  --region $REGION

echo " Stack creado correctamente."

# Obtener DNS del Load Balancer
DNS=$(aws cloudformation describe-stacks \
  --stack-name $STACK_NAME \
  --region $REGION \
  --query "Stacks[0].Outputs[?OutputKey=='LoadBalancerDNS'].OutputValue" \
  --output text)

if [ -z "$DNS" ]; then
  echo " No se encontró el DNS del Load Balancer."
  exit 1
fi

echo "Aplicación disponible en: http://$DNS/swagger-ui/index.html"
echo "También puedes probar: http://$DNS"

# Abrir en navegador si estás en entorno gráfico compatible
if which xdg-open &> /dev/null; then
  xdg-open "http://$DNS/swagger-ui/index.html"
elif which open &> /dev/null; then
  open "http://$DNS/swagger-ui/index.html"
fi
