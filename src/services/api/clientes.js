const api = require("./api");

const clientesService = {
  // Registrar nuevo cliente -> backend /users
  async create(data) {
    const response = await api.post("/users", data);
    return response.data;
  },

  // Obtener cliente por ID
  async findById(id) {
    const response = await api.get(`/users/${id}`);
    return response.data;
  },

  // Listar todos los clientes
  async list() {
    const response = await api.get("/users");
    return Array.isArray(response.data) ? response.data : [];
  },

  // Buscar cliente por correo
  async findByEmail(email) {
    const response = await api.get(`/users/by-email?email=${encodeURIComponent(email)}`);
    return response.data;
  },

  // Simple login helper that fetches by email and compares password client-side (dev)
  // Login using backend /auth/login endpoint. Falls back to GET /users/by-email only if POST fails.
  async login(email, password) {
    try {
      const response = await api.post('/auth/login', { email, password });
      return response.data;
    } catch (err) {
      // if server returns 401 or not found, fallback to older behavior
      if (err.response && err.response.status === 401) return null;
      try {
        const user = await this.findByEmail(email);
        if (!user) return null;
        return user.password === password ? user : null;
      } catch (e) {
        return null;
      }
    }
  }
};

module.exports = clientesService;
