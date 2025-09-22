# 🚀 Selenium Java Maven - SauceDemo Automation

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.0+-green.svg)](https://selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.0+-red.svg)](https://testng.org/)

## 📋 Descripción del Proyecto

Este proyecto implementa un framework de automatización de pruebas para la aplicación **SauceDemo** utilizando Selenium WebDriver con Java y Maven. El framework está diseñado siguiendo las mejores prácticas de automatización y patrones de diseño como Page Object Model (POM).

## 🎯 Objetivos

- Automatizar casos de prueba críticos de la aplicación SauceDemo
- Implementar un framework robusto y mantenible
- Generar reportes detallados de las pruebas ejecutadas
- Establecer una base sólida para pruebas de regresión

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 11+ | Lenguaje de programación principal |
| **Maven** | 3.6+ | Gestión de dependencias y construcción |
| **Selenium WebDriver** | 4.0+ | Automatización de navegadores web |
| **TestNG** | 7.0+ | Framework de testing y generación de reportes |
| **WebDriverManager** | 5.0+ | Gestión automática de drivers |
| **ExtentReports** | 5.0+ | Generación de reportes HTML |



## ✨ Características Principales

### 🎭 Patrones de Diseño Implementados

- **Page Object Model (POM)**: Separación clara entre la lógica de pruebas y los elementos de la página
- **Factory Pattern**: Para la creación de instancias de WebDriver
- **Singleton Pattern**: Para la gestión de configuración y drivers
- **Data Provider Pattern**: Para la gestión de datos de prueba

### 🔧 Funcionalidades Clave

- ✅ **Gestión Automática de Drivers**: WebDriverManager elimina la necesidad de descargar drivers manualmente
- ✅ **Ejecución Cross-Browser**: Soporte para Chrome, Firefox, Edge y Safari
- ✅ **Configuración Flexible**: Archivo de propiedades para configuraciones dinámicas
- ✅ **Reportes Avanzados**: ExtentReports con screenshots automáticos en fallos
- ✅ **Logging Detallado**: Sistema de logs para debugging y troubleshooting
- ✅ **Data-Driven Testing**: Soporte para archivos Excel y JSON
- ✅ **Parallel Execution**: Capacidad de ejecutar pruebas en paralelo

## 🚀 Instalación y Configuración

### Prerrequisitos

```bash
# Java 11 o superior
java -version

# Maven 3.6 o superior
mvn -version

# Git
git --version
```

### Clonación e Instalación

```bash
# Clonar el repositorio
git clone https://github.com/tuusuario/saucedemo-automation.git

# Navegar al directorio
cd saucedemo-automation

# Instalar dependencias
mvn clean install

# Compilar el proyecto
mvn compile
```

## ▶️ Ejecución de Pruebas

### Ejecución Básica

```bash
# Ejecutar todas las pruebas
mvn clean test

# Ejecutar con un navegador específico
mvn clean test -Dbrowser=chrome

# Ejecutar un grupo específico de pruebas
mvn clean test -Dgroups=smoke
```

### Ejecución Avanzada

```bash
# Ejecutar en modo headless
mvn clean test -Dheadless=true

# Ejecutar pruebas específicas
mvn clean test -Dtest=LoginTests

# Ejecutar con reportes personalizados
mvn clean test -DgenerateReports=true
```

### Ejecución con TestNG XML

```bash
# Usar configuración específica de TestNG
mvn clean test -DsuiteXmlFile=testng.xml
```

## 📊 Reportes y Resultados

### Tipos de Reportes Generados

1. **ExtentReports**: Reportes HTML interactivos con:
   - Dashboard con métricas de ejecución
   - Screenshots automáticos en fallos
   - Logs detallados por test
   - Gráficos y estadísticas

2. **TestNG Reports**: Reportes nativos de TestNG

3. **Surefire Reports**: Para integración con CI/CD



