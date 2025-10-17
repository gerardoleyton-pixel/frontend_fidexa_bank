"use strict";
document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");
    const correoInput = document.getElementById("correo");
    const contraseñaInput = document.getElementById("contraseña");
    form.addEventListener("submit", (e) => {
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
