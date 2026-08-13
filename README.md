# ParaBank - Automatización de pruebas

Proyecto de automatización de pruebas para el portal bancario **ParaBank**, desarrollado con **Java 21, Playwright y Cucumber**, utilizando el patrón **Page Object Model (POM)**.

La suite cubre las funcionalidades de **registro, inicio de sesión, transferencia y retiro**, utilizando pruebas UI y API según la operación a validar.

---

## Tecnologías utilizadas

| Tecnología | Versión / Uso |
|---|---|
| Java | 21 |
| Maven | 3.9+ |
| Playwright | Automatización UI y API |
| Cucumber | BDD / Gherkin |
| JUnit | Ejecución de pruebas |
| Page Object Model | Diseño de la automatización |

### Aplicación bajo prueba

[ParaBank](https://parabank.parasoft.com/parabank/index.htm)

---

## Funcionalidades automatizadas

La suite incluye escenarios para:

- Registro de nuevos usuarios.
- Validaciones del formulario de registro.
- Inicio de sesión.
- Transferencia entre cuentas.
- Retiro mediante API.
- Validación de saldos.
- Validación de comportamientos defectuosos identificados durante las pruebas.

Los escenarios utilizan datos generados durante la ejecución para reducir la dependencia de información previamente creada en el ambiente.

---

## Requisitos previos

Antes de ejecutar el proyecto se requiere:

- Java JDK 21.
- Maven 3.9 o superior.
- Acceso a Internet.
- Acceso al ambiente público de ParaBank.

Los navegadores requeridos por Playwright se descargan automáticamente según la configuración del proyecto.

La configuración principal se encuentra en:

```text
src/test/resources/config.properties
```

Los valores de configuración también pueden sobrescribirse mediante propiedades de Maven.

---

## Ejecución

### Ejecutar toda la suite

```bash
mvn test
```

### Ejecutar con el navegador visible

```bash
mvn test -Dheadless=false
```

### Ejecutar por funcionalidad

**Registro**

```bash
mvn test -Dcucumber.filter.tags="@registration"
```

**Login**

```bash
mvn test -Dcucumber.filter.tags="@login"
```

**Transferencia**

```bash
mvn test -Dcucumber.filter.tags="@transfer"
```

**Retiro**

```bash
mvn test -Dcucumber.filter.tags="@withdrawal"
```

### Ejecutar pruebas principales

```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

### Otros tags disponibles

```text
@ui
@api
@sut_defect
```

Los escenarios marcados con `@sut_defect` corresponden a comportamientos identificados en el sistema bajo prueba y se mantienen separados de las pruebas funcionales principales.

---

## Estrategia de automatización

Las funcionalidades se automatizan de acuerdo con la forma en que están disponibles en ParaBank:

| Funcionalidad | Tipo de prueba |
|---|---|
| Registro | UI |
| Login | UI |
| Transferencia | UI |
| Retiro | API |
| Validación de saldos | API |

El registro, login y transferencia se realizan mediante la interfaz utilizando Playwright.

El retiro se realiza mediante API debido a que esta operación no está disponible desde la interfaz utilizada en el ejercicio.

La validación de saldos se realiza mediante API para comprobar el resultado de las operaciones.

De esta forma, la automatización combina pruebas **UI y API** de acuerdo con las características de cada funcionalidad.

---

## Buenas prácticas aplicadas

El proyecto utiliza:

- **Page Object Model (POM)** para separar los elementos de la interfaz de los escenarios.
- **Cucumber / Gherkin** para describir los casos de prueba.
- **Hooks** para administrar el ciclo de vida de los escenarios y generar evidencia cuando corresponde.
- **Datos dinámicos** para reducir dependencias entre ejecuciones.
- **Contexto por escenario** para mantener los datos utilizados durante una prueba.
- **Separación de responsabilidades** entre Pages, Steps, API, datos y configuración.
- **Tags de Cucumber** para ejecutar grupos específicos de pruebas.
- **Reportes HTML y JSON** para facilitar la revisión de resultados.

---

## Datos de prueba

Los datos utilizados durante las pruebas se generan durante la ejecución.

La generación de datos se encuentra en:

```text
src/test/java/com/pruebabg/data/GeneradorDeDatos
```

El contexto utilizado para compartir información dentro de un escenario se encuentra en:

```text
src/test/java/com/pruebabg/context/ContextoDelEscenario
```

Esto permite que los escenarios trabajen con sus propios datos y evita depender, en la medida de lo posible, de información creada manualmente antes de ejecutar la suite.

---

## Estructura del proyecto

```text
src/test/java/com/pruebabg/
├── api/          Servicios REST
├── config/       Configuración
├── context/      Contexto de los escenarios
├── data/         Generación de datos
├── driver/       Gestión de Playwright
├── hooks/        Hooks de Cucumber
├── model/        Modelos
├── pages/        Page Objects
├── runners/      Runner de Cucumber
└── steps/        Step Definitions

src/test/resources/
├── config.properties
└── features/
```

---

## Reportes

Los resultados de las pruebas se generan en:

```text
target/cucumber-reports/
```

Se generan los siguientes archivos:

```text
reporte.html
reporte.json
reporte.xml
timeline/
```

El reporte HTML permite consultar los escenarios ejecutados y sus resultados.

Cuando una prueba de interfaz falla, se adjunta una **captura de pantalla como evidencia**.

---

## Hallazgos durante las pruebas

Durante la ejecución se identificaron algunos comportamientos del ambiente público de ParaBank que se consideran defectos del sistema bajo prueba.

Entre ellos:

- Comportamiento inesperado durante el inicio de sesión con credenciales inválidas.
- Problemas en la visualización de la información de `Accounts Overview`.

Estos escenarios se encuentran identificados mediante:

```text
@sut_defect
```

Esto permite diferenciarlos de posibles errores relacionados con la automatización.

---

## Ejecución de los defectos identificados

Para ejecutar únicamente estos escenarios:

```bash
mvn test -Dcucumber.filter.tags="@sut_defect"
```

---

## Consideraciones

La ejecución depende de la disponibilidad del ambiente público de ParaBank. Algunos resultados pueden verse afectados por cambios o problemas propios del ambiente.

Los escenarios identificados como `@sut_defect` corresponden a hallazgos realizados durante la ejecución y no necesariamente a problemas relacionados con el framework de automatización.
