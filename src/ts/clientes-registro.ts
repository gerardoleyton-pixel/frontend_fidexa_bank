document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector("form") as HTMLFormElement;
  const nombreInput = document.getElementById("nombre") as HTMLInputElement;
  const correoInput = document.getElementById("correo") as HTMLInputElement;
  const contraseñaInput = document.getElementById("contraseña") as HTMLInputElement;

  form.addEventListener("submit", (e: Event) => {
    const nombre = nombreInput.value.trim();
    const correo = correoInput.value.trim();
    const contraseña = contraseñaInput.value;

    if (!nombre || !correo || !contraseña) {
      alert("Todos los campos son obligatorios.");
      e.preventDefault();
      return;
    }

    const correoValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(correo);
    if (!correoValido) {
      alert("El correo electrónico no tiene un formato válido.");
      e.preventDefault();
    }

    if (contraseña.length < 6) {
      alert("La contraseña debe tener al menos 6 caracteres.");
      e.preventDefault();
    }
  });
});
