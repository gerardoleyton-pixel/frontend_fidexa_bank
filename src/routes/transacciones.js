const express = require("express");
const router = express.Router();
const transaccionesService = require("../services/api/transacciones");
const ensureAuthenticated = require('../middleware/auth');
const flash = require('../middleware/flash');

// Listar transacciones por cuenta
router.get("/cuenta/:id", ensureAuthenticated, async (req, res, next) => {
  try {
    const movimientos = await transaccionesService.listByAccount(req.params.id);
    res.render("transacciones-lista", {
      movimientos,
      cuentaId: req.params.id,
    });
  } catch (error) {
    console.error("❌ Error al listar transacciones:", error.message);
    next(error);
  }
});

// Mostrar formulario de depósito
router.get("/cuenta/:id/deposito", ensureAuthenticated, (req, res) => {
  res.render("transacciones-deposito", { cuentaId: req.params.id });
});

// Realizar depósito
router.post("/cuenta/:id/deposito", async (req, res, next) => {
  const { amount } = req.body;

  if (!amount || isNaN(parseFloat(amount))) {
    return res.status(400).render("transacciones-deposito", {
      cuentaId: req.params.id,
      error: "El monto es obligatorio y debe ser numérico.",
    });
  }

  try {
    await transaccionesService.deposit({
      amount: parseFloat(amount),
      accountId: req.params.id,
    });
    flash.set(req, 'success', 'Depósito realizado correctamente');
    res.redirect(`/transacciones/cuenta/${req.params.id}`);
  } catch (error) {
    console.error("❌ Error al realizar depósito:", error.message);
    next(error);
  }
});

// Mostrar formulario de retiro
router.get("/cuenta/:id/retiro", ensureAuthenticated, (req, res) => {
  res.render("transacciones-retiro", { cuentaId: req.params.id });
});

// Realizar retiro
router.post("/cuenta/:id/retiro", async (req, res, next) => {
  const { amount } = req.body;

  if (!amount || isNaN(parseFloat(amount))) {
    return res.status(400).render("transacciones-retiro", {
      cuentaId: req.params.id,
      error: "El monto es obligatorio y debe ser numérico.",
    });
  }

  try {
    await transaccionesService.withdraw({
      amount: parseFloat(amount),
      accountId: req.params.id,
    });
    flash.set(req, 'success', 'Retiro realizado correctamente');
    res.redirect(`/transacciones/cuenta/${req.params.id}`);
  } catch (error) {
    console.error("❌ Error al realizar retiro:", error.message);
    next(error);
  }
});

// Mostrar formulario de transferencia
router.get("/cuenta/:id/transferencia", ensureAuthenticated, (req, res) => {
  res.render("transacciones-transferencia", { origenId: req.params.id });
});

// Realizar transferencia
router.post("/cuenta/:id/transferencia", async (req, res, next) => {
  const { amount, destinoId } = req.body;

  if (!amount || isNaN(parseFloat(amount)) || !destinoId) {
    return res.status(400).render("transacciones-transferencia", {
      origenId: req.params.id,
      error: "Monto y cuenta destino son obligatorios.",
    });
  }

  try {
    await transaccionesService.transfer({
      amount: parseFloat(amount),
      fromAccountId: req.params.id,
      toAccountId: destinoId,
    });
    flash.set(req, 'success', 'Transferencia realizada correctamente');
    res.redirect(`/transacciones/cuenta/${req.params.id}`);
  } catch (error) {
    console.error("❌ Error al realizar transferencia:", error.message);
    next(error);
  }
});

module.exports = router;
