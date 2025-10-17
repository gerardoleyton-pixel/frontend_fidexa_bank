const api = require("./api");

const transaccionesService = {
  // Listar todas las transacciones
  async list() {
    const response = await api.get("/transactions");
    return Array.isArray(response.data) ? response.data : [];
  },

  // Buscar transacción por ID
  async findById(id) {
    const response = await api.get(`/transactions/${id}`);
    return response.data;
  },

  // Listar transacciones por cuenta
  async listByAccount(accountId) {
    const response = await api.get(`/transactions/by-account?accountId=${accountId}`);
    return Array.isArray(response.data) ? response.data : [];
  },

  // Listar transacciones por tipo
  async listByType(type) {
    const response = await api.get(`/transactions/by-type?type=${type}`);
    return Array.isArray(response.data) ? response.data : [];
  },

  // Listar transacciones por rango de fechas
  async listByDateRange(start, end) {
    const response = await api.get(`/transactions/by-date-range?start=${start}&end=${end}`);
    return Array.isArray(response.data) ? response.data : [];
  },

  // Realizar depósito
  async deposit(data) {
    // backend expects { accountId, amount }
    const payload = { accountId: data.accountId || data.bankAccountId || data.bankAccountId, amount: data.amount };
    const response = await api.post("/transactions/deposit", payload);
    return response.data;
  },

  // Realizar retiro
  async withdraw(data) {
    const payload = { accountId: data.accountId || data.bankAccountId, amount: data.amount };
    const response = await api.post("/transactions/withdraw", payload);
    return response.data;
  },

  // Realizar transferencia
  async transfer(data) {
    // backend expects { fromAccountId, toAccountId, amount }
    const payload = {
      fromAccountId: data.fromAccountId || data.origenId || data.origen_id || data.origen,
      toAccountId: data.toAccountId || data.destinoId || data.destino,
      amount: data.amount,
    };
    const response = await api.post("/transactions/transfer", payload);
    return response.data;
  }
};

module.exports = transaccionesService;
