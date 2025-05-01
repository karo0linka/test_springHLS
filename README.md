# 🚀 Spring Boot en AWS ECS con RDS y CI/CD (GitHub Actions + OIDC)

![Build & Deploy](https://github.com/lbribiesca-hls/springboot-app/actions/workflows/deploy.yml/badge.svg)
![Coverage](https://img.shields.io/badge/coverage-100%25-brightgreen)
[![Quality Gate Status](https://sonarqube.hlsgroup.com.mx/api/project_badges/measure?project=springboot-app&metric=alert_status)](https://sonarqube.hlsgroup.com.mx/dashboard?id=springboot-app)

Este proyecto despliega una aplicación Spring Boot sobre ECS Fargate usando:

- ✅ Docker + Amazon ECR
- ✅ Amazon ECS (Fargate)
- ✅ Load Balancer (ALB)
- ✅ Amazon RDS PostgreSQL (Free Tier compatible)
- ✅ GitHub Actions con autenticación OIDC
- ✅ Análisis SonarQube con cobertura del 100%
- ✅ Tests automatizados con JUnit, Mockito y Testcontainers

---

## 📦 Requisitos previos

- AWS CLI configurado (`aws configure`)
- Cuenta GitHub con acceso al repo `lbribiesca-hls/springboot-app`
- Rol OIDC creado en IAM
- Docker instalado localmente para pruebas
- SonarQube accesible en [https://sonarqube.hlsgroup.com.mx](https://sonarqube.hlsgroup.com.mx)

---

## 🏗️ Despliegue de Infraestructura

1. **Construir y subir imagen a ECR (opcional si CI lo hará):**
```bash
./build-ecr.sh
```

2. **Desplegar CloudFormation completo (ECS + RDS + ALB):**
```bash
./deploy-stack.sh
```

3. **Obtener valores requeridos para el CI/CD (manual):**
```bash
aws ecs list-services \
--cluster springboot-cluster

aws ecs list-task-definitions \
--query "taskDefinitionArns[?contains(@, 'springboot')]" \
--output text
```

4. **Eliminar toda la infraestructura:**
```bash
./delete-stack.sh
aws cloudformation delete-stack \
--stack-name github-oidc-role
```

---

## 🔐 Configuración OIDC + IAM para GitHub Actions

### 1. Crear OIDC Provider (solo una vez por cuenta):

```bash
aws iam create-open-id-connect-provider   \
--url https://token.actions.githubusercontent.com   \
--client-id-list sts.amazonaws.com   \
--thumbprint-list 6938fd4d98bab03faadb97b34396831e3780aea1
```

### 2. Crear rol IAM con CloudFormation:
```bash
aws cloudformation deploy   \
--stack-name github-oidc-role   \
--template-file github-oidc-role.yaml  \
--capabilities CAPABILITY_NAMED_IAM
```

> Asegúrate de editar `github-oidc-role.yaml` con tu repositorio (`user/repo`) y rama (`dev`).

---

## 🔁 CI/CD con GitHub Actions

### Archivo: `.github/workflows/deploy.yml`

Este flujo:

1. Construye la imagen Docker
2. Ejecuta pruebas unitarias y de integración
3. Ejecuta análisis SonarQube (con Quality Gate obligatorio)
4. Sube la imagen a Amazon ECR
5. Registra nueva Task Definition
6. Actualiza el ECS Service

📍 **Valores clave en el YAML**:
- `role-to-assume`: ARN del rol OIDC (`GitHubActionsOIDC`)
- `ECS_SERVICE`: Nombre del servicio ECS
- `TASK_FAMILY`: Nombre del Task Definition

---

## 🧪 Pruebas automatizadas

Se ejecutan durante el flujo de CI/CD:

- `UserControllerTest`: pruebas de endpoints usando MockMvc
- `UserRepositoryTest`: test con `@DataJpaTest` y H2
- `UserServiceTest`: test con `Mockito` + `@InjectMocks`

> Todas las pruebas están ubicadas en `src/test/java/`.

---

## 📁 Estructura esperada del repositorio

```
.
├── Dockerfile
├── docker-compose.yml
├── deploy-stack.sh
├── delete-stack.sh
├── github-oidc-role.yaml
├── springboot-ecs-alb.yaml
├── pom.xml
├── src/
│   ├── main/java/...
│   └── test/java/...
└── .github/
    └── workflows/
        └── deploy.yml
```

---

## 🧹 Limpieza

```bash
./delete-stack.sh
aws cloudformation delete-stack \
--stack-name github-oidc-role
```

---

✅ **Versión `v1.0.0` desplegada automáticamente con CI/CD seguro, auditado y validado en SonarQube.**
