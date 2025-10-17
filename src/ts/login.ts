document.addEventListener("DOMContentLoaded", () => {
  const form = document.querySelector("form") as HTMLFormElement;
  const correoInput = document.getElementById("correo") as HTMLInputElement;
  const contraseñaInput = document.getElementById("contraseña") as HTMLInputElement;

  form.addEventListener("submit", (e: Event) => {
    const correo = correoInput.value.trim();
    const contraseña = contraseñaInput.value;

    if (!correo || !contraseña) {
      alert("Correo y contraseña son obligatorios.");
      e.preventDefault();
      return;
    }

    const correoValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(correo);
    if (!correoValido) {
      alert("El correo electrónico no tiene un formato válido.");
      e.preventDefault();
    }
  });
});
