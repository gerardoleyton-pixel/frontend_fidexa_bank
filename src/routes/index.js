const express = require("express");
const router = express.Router();

// Ruta raíz: muestra la página de bienvenida
router.get("/", (req, res) => {
  res.render("bienvenida");
});

module.exports = router;
