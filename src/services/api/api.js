const axios = require("axios");
require("dotenv").config();

const api = axios.create({
  baseURL: process.env.API_BASE_URL || "http://localhost:3000/api",
  timeout: 5000,
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error("Error en la respuesta del backend:", error.message);
    return Promise.reject(error);
  }
);

module.exports = api;
