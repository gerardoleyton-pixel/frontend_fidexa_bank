# Fidexa React (modernized frontend)

This is a Vite + React 18 frontend that consumes the Java/Spring backend API used by the previous `fidexa-web` app.

Quick start

1. Install dependencies (Node.js 20+ recommended):

```powershell
cd c:\Users\user\Documents\frontend\fidexa-react
npm install
```

2. Copy `.env.example` to `.env` and set the API base URL if necessary.

3. Run dev server:

```powershell
npm run dev
```

4. Run tests:

```powershell
npm run test
```

What is included

- React 18 + Vite
- React Router for client-side routing
- Axios with centralized interceptor and service modules
- Components and pages for Accounts and Transactions (list, create, detail, update)
- Basic authentication flow using the backend `/auth/login` endpoint (session kept in localStorage)
- Simple tests with Vitest + Testing Library

Notes

- The app reads the API base URL from `import.meta.env.VITE_API_BASE_URL` or `process.env.REACT_APP_API_BASE_URL` (for compatibility).
- If the backend enforces CORS, ensure it allows the frontend origin (http://localhost:5173).
