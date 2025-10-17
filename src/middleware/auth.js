module.exports = function ensureAuthenticated(req, res, next) {
  if (req.session && req.session.user) return next();
  req.session.flash = { type: 'error', message: 'Debes iniciar sesión para acceder a esa página.' };
  return res.redirect('/clientes/login');
};
