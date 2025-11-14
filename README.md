# 🃏 Cincuentazo

**Cincuentazo** es un mini–proyecto en Java que implementa un juego de cartas tipo póker donde un jugador humano se enfrenta contra **1, 2 o 3 jugadores máquina**.  
El objetivo es **sobrevivir** turno a turno sin dejar que la suma de las cartas en la mesa supere el valor de **50**.

El proyecto está desarrollado con **JavaFX**, sigue la arquitectura **MVC**, utiliza **hilos** para la concurrencia, **excepciones personalizadas**, y **pruebas unitarias con JUnit 5**, gestionado con **Maven**, **Git** y **GitHub**.

---

## 🎮 Reglas del juego

### Preparación inicial

- Se utiliza un mazo estándar de cartas de póker (sin comodines).
- Se reparten **4 cartas aleatorias** a cada jugador:
  - Jugador humano: cartas **boca arriba**.
  - Jugadores máquina: cartas **boca abajo**.
- Se coloca **una carta aleatoria en la mesa**, boca arriba:
  - Esta carta inicial define la **suma de la mesa**.
- El resto de cartas quedan en el **mazo** boca abajo.

La suma de la mesa puede comenzar en:
- `0` → si la carta inicial es un **9**.
- `1` → si la carta inicial es un **A** (as).
- `-10` → si la carta inicial es **J**, **Q** o **K**.

---

### Valores de las cartas

En cada turno, la carta jugada modifica la **suma de la mesa** según:

- Cartas **2, 3, 4, 5, 6, 7, 8 y 10**  
  ➜ **Suman** su valor numérico.
- Carta **9**  
  ➜ **No suma ni resta** (`+0`).
- Cartas **J, Q, K**  
  ➜ **Restan 10** (`-10`).
- Carta **A (As)**  
  ➜ Suma **1 o 10**, según convenga para **no exceder 50**.

---

### Turno de juego

En su turno, cada jugador debe:

1. **Elegir una carta de su mano** teniendo en cuenta:
   - La carta modificará la **suma de la mesa**.
   - **No se puede superar 50**; si una carta hace que la suma pase de 50, esa carta no es jugable.
2. La carta seleccionada se coloca **boca arriba en la mesa**, encima de la anterior.
3. Se actualiza la **suma de la mesa** según el valor de la carta.
4. El jugador **roba una carta del mazo** para volver a tener **4 cartas en la mano** (si hay cartas disponibles).

🔴 **Eliminación**:  
Si un jugador **no puede jugar ninguna de sus 4 cartas** porque todas harían que la suma supere 50, ese jugador queda **eliminado** de la partida.

---

### Mazo y reciclaje de cartas

- Si **se acaban las cartas del mazo**:
  - Se toman **todas las cartas de la mesa, excepto la última jugada**.
  - Se **barajan** y se vuelven a colocar como nuevo mazo boca abajo.
  - La **suma de la mesa NO se reinicia** (se mantiene la suma actual).
- Cuando un jugador es **eliminado**:
  - Sus cartas en mano se envían al **final del mazo**, quedando disponibles para ser robadas.

---

### Fin del juego

- El juego continúa por turnos mientras haya **al menos 2 jugadores** activos.
- Gana la partida el **último jugador que permanece en juego** (humano o máquina).

---

## 🎯 Objetivos del proyecto

### Objetivo general

> Desarrollar el juego **Cincuentazo** aplicando principios de **programación orientada a eventos** y **diseño de interfaces gráficas con JavaFX**, integrando **arquitectura MVC**, uso de **hilos** para concurrencia, manejo robusto de **excepciones**, y **pruebas unitarias con JUnit 5**, gestionando el código de manera profesional con **Git** y **GitHub**.

### Objetivos específicos

- Diseñar una **interfaz gráfica intuitiva y usable** utilizando layouts de JavaFX.
- Implementar **eventos de mouse y teclado** para la interacción del jugador.
- Emplear **interfaces**, clases internas y **adaptadoras** en la gestión de eventos.
- Aplicar la arquitectura **Modelo–Vista–Controlador (MVC)** para estructurar el proyecto.
- Implementar una **estructura de datos dinámica** que administre cartas y mazo.
- Documentar el código en inglés con **Javadoc**.
- Implementar **excepciones propias** (checked y unchecked) para controlar errores en el flujo del juego.
- Aplicar al menos **dos hilos** (ej. temporización de la máquina, control visual de turnos).
- Crear **tres clases de pruebas unitarias** con **JUnit 5** para validar la lógica del juego.
- Gestionar el proyecto con **Git y GitHub**, manteniendo un repositorio **bien documentado**.

---

## 🧱 Arquitectura y organización del código

El proyecto está organizado como una aplicación Java modular con JavaFX y Maven:

```text
src/
└── main
    ├── java
    │   ├── module-info.java
    │   └── com/example/minip3poe
    │       ├── Main.java              # Punto de entrada JavaFX
    │       ├── controller/            # Controladores (lógica de UI y eventos)
    │       │   └── threads/           # Tareas en segundo plano (hilos)
    │       ├── model/                 # Lógica de negocio y reglas del juego
    │       │   ├── exceptions/        # Excepciones personalizadas
    │       │   └── player/            # Interfaces y clases de jugadores
    │       └── view/                  # Stages y manejo de escenas JavaFX
    ├── resources
    │   └── com/example/minip3poe      # FXML, estilos e imágenes
    └── test
        └── ...                        # Pruebas unitarias JUnit 5
Requisitos técnicos

Java: JDK 17
JavaFX: 17.x (configurado vía Maven)
Maven: 3.8+
IDE recomendado:
IntelliJ IDEA / Eclipse / NetBeans con soporte para Maven y JavaFX.

Autores

Juan David Salazar
Veronica Granados
Freddy Alexander Melo Buitrago
