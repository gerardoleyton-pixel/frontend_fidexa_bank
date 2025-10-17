const express = require("express");
const router = express.Router();
const cuentasService = require("../services/api/cuentas");
const transaccionesService = require("../services/api/transacciones");
const ensureAuthenticated = require('../middleware/auth');
const flash = require('../middleware/flash');

// Listar todas las cuentas
router.get("/", ensureAuthenticated, async (req, res, next) => {
  try {
    // Mostrar sólo las cuentas del usuario autenticado
    const userId = req.session && req.session.user && req.session.user.id;
    if (!userId) {
      return res.redirect('/login');
    }
    const cuentas = await cuentasService.listByUser(userId);
    res.render("cuentas-lista", { cuentas });
  } catch (error) {
    console.error("Error al listar cuentas:", error.message);
    next(error);
  }
});

// Mostrar formulario para crear cuenta
router.get("/nueva", ensureAuthenticated, (req, res) => {
  res.render("cuentas-nueva");
});

// Crear cuenta
router.post("/nueva", ensureAuthenticated, async (req, res, next) => {
  const { titular, numeroCuenta, saldoInicial, userId } = req.body;
  const ownerId = userId || (req.session.user && req.session.user.id);

  if (!titular || !numeroCuenta || !saldoInicial || !ownerId) {
    return res.status(400).render("cuentas-nueva", {
      error: "Todos los campos son obligatorios.",
    });
  }

  try {
    await cuentasService.create({
      accountHolder: titular,
      accountNumber: numeroCuenta,
      initialBalance: saldoInicial,
      userId: ownerId,
    });
    flash.set(req, 'success', 'Cuenta creada correctamente');
    res.redirect("/cuentas");
  } catch (error) {
    console.error("❌ Error al crear cuenta:", error.message);
    res.status(500).render("cuentas-nueva", {
      error: "Hubo un problema al crear la cuenta.",
    });
  }
});

// Ver detalle de cuenta
router.get("/:id", ensureAuthenticated, async (req, res, next) => {
  try {
    const cuenta = await cuentasService.findById(req.params.id);
    // Obtener movimientos asociados a esta cuenta para mostrarlos en el detalle
    const movimientos = await transaccionesService.listByAccount(req.params.id);
    res.render("cuentas-detalle", { cuenta, movimientos });
  } catch (error) {
    console.error("Error al obtener detalle de cuenta:", error.message);
    next(error);
  }
});

// Actualizar cuenta
router.post("/:id", async (req, res, next) => {
  const { accountHolder, balance } = req.body;

  if (!accountHolder || balance === undefined) {
    return res.status(400).render("cuentas-detalle", {
      error: "Titular y saldo son obligatorios.",
      cuenta: { id: req.params.id, accountHolder, balance },
    });
  }

  try {
    await cuentasService.update(req.params.id, { accountHolder, balance });
    res.redirect(`/cuentas/${req.params.id}`);
  } catch (error) {
    console.error("Error al actualizar cuenta:", error.message);
    next(error);
  }
});

module.exports = router;
