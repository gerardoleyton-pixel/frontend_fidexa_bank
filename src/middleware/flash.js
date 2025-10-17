// Helper to set flash messages
module.exports = {
  set: function (req, type, message) {
    req.session.flash = { type, message };
  }
};
