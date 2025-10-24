import axios from 'axios'

const base = import.meta.env.VITE_API_BASE_URL || process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080'

const api = axios.create({
  baseURL: base,
  headers: { 'Content-Type': 'application/json' }
})

// simple interceptor for responses and errors
api.interceptors.response.use(
  response => response,
  error => {
    // centralized error parsing
    if (error.response && error.response.data) {
      const msg = error.response.data.message || error.response.data.error || JSON.stringify(error.response.data)
      error.message = msg
    }
    return Promise.reject(error)
  }
)

export default api
