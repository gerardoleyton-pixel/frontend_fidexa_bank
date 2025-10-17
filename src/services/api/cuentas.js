const api = require("./api");

const cuentasService = {
  // Obtener todas las cuentas
  async list() {
    const response = await api.get("/accounts");
    return Array.isArray(response.data) ? response.data : [];
  },

  // Crear una nueva cuenta
  async create(data) {
    // backend expects userId, accountHolder, accountNumber, balance
    const payload = {
      userId: data.userId || data.usuarioId || data.userId,
      accountHolder: data.accountHolder || data.titular,
      accountNumber: data.accountNumber || data.numeroCuenta,
      // backend expects initialBalance
      initialBalance: data.initialBalance || data.balance || data.saldoInicial,
    };
    const response = await api.post("/accounts", payload);
    return response.data;
  },

  // Buscar cuenta por ID
  async findById(id) {
    const response = await api.get(`/accounts/${id}`);
    return response.data;
  },

  // Actualizar cuenta por ID
  async update(id, data) {
    const response = await api.put(`/accounts/${id}`, data);
    return response.data;
  },

  // Eliminar cuenta por ID
  async remove(id) {
    const response = await api.delete(`/accounts/${id}`);
    return response.data;
  },

  // Listar cuentas por ID de usuario
  async listByUser(userId) {
    const response = await api.get(`/accounts/by-user?userId=${userId}`);
    return Array.isArray(response.data) ? response.data : [];
  }
};

module.exports = cuentasService;
