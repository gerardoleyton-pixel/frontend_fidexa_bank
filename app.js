const express = require("express");
const path = require("path");
const dotenv = require("dotenv");
const expressLayouts = require("express-ejs-layouts");
const cookieParser = require("cookie-parser");
const session = require("express-session");

dotenv.config();

const app = express();

// Middleware
app.use(express.urlencoded({ extended: true }));
app.use(express.json());
app.use(cookieParser());

// Session (development-friendly defaults; set SECRET in .env for production)
app.use(
  session({
    secret: process.env.SESSION_SECRET || "dev-secret",
    resave: false,
    saveUninitialized: false,
    cookie: { maxAge: 1000 * 60 * 60 * 2 }, // 2 horas
  })
);

// Small flash/message helper using session (no extra dependency)
app.use((req, res, next) => {
  res.locals.flash = req.session.flash || null;
  delete req.session.flash;
  res.locals.user = req.session.user || null;
  next();
});

// Configuración de vistas
app.set("view engine", "ejs");
app.set("views", path.join(__dirname, "src/views"));
app.set("layout", "layout");
app.use(expressLayouts);

// Archivos estáticos
app.use("/css", express.static(path.join(__dirname, "src/public/css")));
app.use("/js", express.static(path.join(__dirname, "src/public/js")));
app.use("/images", express.static(path.join(__dirname, "src/public/images")));

// Rutas
const indexRoutes = require("./src/routes/index");
const clientesRoutes = require("./src/routes/clientes");
const cuentasRoutes = require("./src/routes/cuentas");
const transaccionesRoutes = require("./src/routes/transacciones");

app.use("/", indexRoutes);
app.use("/clientes", clientesRoutes);
app.use("/cuentas", cuentasRoutes);
app.use("/transacciones", transaccionesRoutes);

// Errores
app.use((err, req, res, next) => {
  console.error("Error:", err && err.message ? err.message : err);
  res.status(500).render("error", { mensaje: "Ocurrió un error en el servidor." });
});

// Servidor
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Servidor Express activo en http://localhost:${PORT}`);
});
