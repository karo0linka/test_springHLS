#!/bin/bash

STACK_NAME="springboot-ecs-alb"
REGION="us-east-1"

echo "Esto eliminará el stack '$STACK_NAME' y todos los recursos asociados."
read -p "¿Estás seguro de continuar? (s/n): " confirm

if [[ "$confirm" != "s" && "$confirm" != "S" ]]; then
  echo "Cancelado por el usuario."
  exit 0
fi

echo "Eliminando stack: $STACK_NAME ..."
aws cloudformation delete-stack \
  --stack-name $STACK_NAME \
  --region $REGION

echo "Esperando a que se complete la eliminación..."
aws cloudformation wait stack-delete-complete \
  --stack-name $STACK_NAME \
  --region $REGION

echo "Stack eliminado exitosamente."
