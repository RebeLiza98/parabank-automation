# ParaBank — Automatización de pruebas

Proyecto de automatización para el portal bancario **ParaBank**, desarrollado con **Java 21, Playwright y Cucumber**, utilizando el patrón **Page Object Model**.

La automatización cubre las principales operaciones solicitadas: **registro, login, transferencia y retiro**, combinando pruebas de interfaz y API cuando es necesario.

---

## Lo que hace este proyecto

La suite permite probar:

* Registro de nuevos usuarios.
* Validaciones del formulario de registro.
* Inicio de sesión.
* Transferencias entre cuentas.
* Retiro mediante API.
* Validación de saldos.
* Comportamientos defectuosos encontrados en el portal.

Los datos de prueba se generan automáticamente para evitar depender de usuarios o cuentas creadas anteriormente.

---

## ANTES DE EJECUTAR

Se necesita:

| Herramienta | Versión                           |
| ----------- | --------------------------------- |
| Java        | 21                                |
| Maven       | 3.9+                              |
| Playwright    |Se instala mediante las dependencias del proyecto|
	
El proyecto utiliza el portal:

```text
https://parabank.parasoft.com/parabank/index.htm
```

Los navegadores de Playwright se descargan automáticamente.

La configuración principal está en:

```text
src/test/resources/config.properties
```

Los valores también pueden sobrescribirse mediante `-D`.

---

## CÓMO EJECUTAR

Para ejecutar toda la suite:

```bash
mvn test
```

Para ejecutar viendo el navegador:

```bash
mvn test -Dheadless=false
```

Por funcionalidad:

```bash
mvn test -Dcucumber.filter.tags="@registration"
mvn test -Dcucumber.filter.tags="@login"
mvn test -Dcucumber.filter.tags="@transfer"
mvn test -Dcucumber.filter.tags="@withdrawal"
```

Para ejecutar solamente las pruebas principales:

```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

También están disponibles:

```text
@ui
@api
@sut_defect
```

Los escenarios `@sut_defect` corresponden a problemas encontrados en el ambiente de ParaBank.

---

## QUÉ HACE LA PRUEBA

Las pruebas de **registro, login y transferencia** se realizan mediante la interfaz utilizando Playwright.

El **retiro** se realiza mediante el servicio REST, ya que ParaBank no dispone de esta operación desde la interfaz.

Los saldos se comprueban mediante API para tener una referencia más confiable, ya que la pantalla `Accounts Overview` presenta problemas en el ambiente público.

Los escenarios utilizan datos generados automáticamente y cada uno mantiene su propio contexto para evitar dependencias entre pruebas.

---

## CRITERIOS QUE DEBE CUMPLIR

* Los escenarios deben poder ejecutarse de forma independiente.
* No se deben utilizar usuarios o cuentas fijas.
* Los datos de prueba deben generarse automáticamente.
* Los selectores deben estar dentro de los Page Objects.
* Las operaciones de UI deben realizarse mediante Playwright.
* Las operaciones que no existen en UI pueden realizarse mediante API.
* Los saldos deben validarse contra el servicio REST.
* Los fallos de UI deben dejar evidencia mediante captura de pantalla.

---

## DATOS DE PRUEBA

Los datos se generan automáticamente durante la ejecución.

La generación se encuentra principalmente en:

```text
src/test/java/com/pruebabg/data/GeneradorDeDatos
```

El estado de cada escenario se mantiene mediante:

```text
src/test/java/com/pruebabg/context/ContextoDelEscenario
```

De esta manera, cada ejecución puede crear sus propios usuarios y trabajar con sus cuentas sin depender de datos anteriores.

---

## ESTRUCTURA PRINCIPAL

```text
src/test/java/com/pruebabg/
├── api/          Servicios REST
├── config/       Configuración
├── context/      Estado del escenario
├── data/         Datos de prueba
├── driver/       Playwright
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

## DÓNDE QUEDAN LOS REPORTES

Los reportes se generan en:

```text
target/cucumber-reports/
```

Incluyen:

```text
reporte.html
reporte.json
reporte.xml
timeline/
```

El reporte HTML permite revisar los escenarios ejecutados y, cuando una prueba de interfaz falla, se adjunta una **captura de pantalla** como evidencia.

---

## NOTAS

Durante las pruebas se detectaron algunos problemas en el ambiente público de ParaBank, principalmente relacionados con el login y la pantalla `Accounts Overview`.

Estos casos están identificados con `@sut_defect` para diferenciarlos de los errores propios de la automatización.

Para ejecutar la suite completa:

```bash
mvn test
```
