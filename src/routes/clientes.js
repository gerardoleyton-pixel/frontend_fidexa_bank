const express = require("express");
const router = express.Router();
const clientesService = require("../services/api/clientes");

// Mostrar formulario de registro de cliente
router.get("/registro", (req, res) => {
  res.render("clientes-registro");
});

// Procesar registro de cliente
router.post("/registro", async (req, res) => {
  const { username, email, password, fullName } = req.body;

  if (!username || !email || !password || !fullName) {
    return res.status(400).render("clientes-registro", {
      error: "Todos los campos son obligatorios.",
    });
  }

  try {
    await clientesService.create({ username, email, password, fullName });
    // flash message and redirect
    req.session.flash = { type: 'success', message: 'Registro exitoso. Por favor inicia sesión.' };
    res.redirect("/clientes/login");
  } catch (error) {
    console.error("Error al registrar cliente:", error.message);
    res.status(500).render("clientes-registro", {
      error: "Hubo un problema al registrar el cliente.",
    });
  }
});

// Mostrar formulario de inicio de sesión
router.get("/login", (req, res) => {
  res.render("login");
});

// Procesar inicio de sesión
router.post("/login", async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).render("login", {
      error: "Correo y contraseña son obligatorios.",
    });
  }

  try {
    // use the login helper which compares password (dev mode)
    const cliente = await clientesService.login(email, password);
    if (!cliente) {
      return res.status(401).render("login", {
        error: "Credenciales inválidas.",
      });
    }

    // Guardar usuario en sesión
    req.session.user = {
      id: cliente.id,
      username: cliente.username,
      email: cliente.email,
      fullName: cliente.fullName
    };

    res.redirect("/cuentas");
  } catch (error) {
    console.error("Error al iniciar sesión:", error.message);
    res.status(500).render("login", {
      error: "Hubo un problema al iniciar sesión.",
    });
  }
});

// Logout
router.post('/logout', (req, res) => {
  req.session.destroy(err => {
    res.redirect('/');
  });
});

module.exports = router;
