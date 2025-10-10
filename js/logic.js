// Mostrar mensaje visual en la página
function mostrarMensaje(id, texto, tipo = "info") {
  const contenedor = document.getElementById(id);
  if (contenedor) {
    contenedor.textContent = texto;
    contenedor.style.color = tipo === "error" ? "#D32F2F" : "#00796B";
    contenedor.style.fontWeight = "500";
    contenedor.style.marginTop = "1rem";
  }
}

// Validación de registro y guardado en localStorage
function validateRegistration() {
  const username = document.getElementById("username")?.value.trim();
  const name = document.getElementById("name")?.value.trim();
  const email = document.getElementById("email")?.value.trim();
  const password = document.getElementById("password")?.value.trim();

  if (!username || !name || !email || !password) {
    mostrarMensaje("registerMessage", "Please complete all fields before submitting.", "error");
    return;
  }

  const userData = { username, name, email, password };
  localStorage.setItem("fidexaUser", JSON.stringify(userData));

  mostrarMensaje("registerMessage", "Registration successful! Redirecting to login...");
  setTimeout(() => {
    window.location.href = "login.html";
  }, 1500);
}

// Validación de login y comparación con localStorage
function validateLogin() {
  const username = document.getElementById("username")?.value.trim();
  const password = document.getElementById("password")?.value.trim();

  if (!username || !password) {
    mostrarMensaje("loginMessage", "Please fill in both username and password.", "error");
    return;
  }

  const storedUser = JSON.parse(localStorage.getItem("fidexaUser"));

  if (storedUser && username === storedUser.username && password === storedUser.password) {
    mostrarMensaje("loginMessage", "Login successful! Redirecting to your account...");
    setTimeout(() => {
      window.location.href = "account.html";
    }, 1500);
  } else if (username === "admin" && password === "admin123") {
    window.location.href = "admin.html";
  } else {
    mostrarMensaje("loginMessage", "Incorrect credentials. Try again.", "error");
  }
}

// Crear cuenta bancaria con monto ingresado
function createAccount() {
  const user = JSON.parse(localStorage.getItem("fidexaUser"));
  const accountKey = `account_${user.email}`;
  if (localStorage.getItem(accountKey)) {
    mostrarMensaje("accountMessage", "Account already exists.", "info");
    return;
  }
  const input = prompt("Enter initial deposit amount:");
  const amount = parseFloat(input);
  if (isNaN(amount) || amount <= 0) {
    mostrarMensaje("accountMessage", "Invalid amount. Please enter a positive number.", "error");
    return;
  }
  const account = { balance: amount };
  localStorage.setItem(accountKey, JSON.stringify(account));
  mostrarMensaje("accountMessage", `Account created with $${amount.toFixed(2)}.`);
  updateBalanceDisplay();
}

// Mostrar saldo actual
function updateBalanceDisplay() {
  const user = JSON.parse(localStorage.getItem("fidexaUser"));
  const accountKey = `account_${user.email}`;
  const account = JSON.parse(localStorage.getItem(accountKey));
  const balance = account?.balance ?? 0;
  const display = document.getElementById("balanceDisplay");
  if (display) {
    display.textContent = `$${balance.toFixed(2)}`;
  }
}

// Simular depósito
function deposit() {
  const user = JSON.parse(localStorage.getItem("fidexaUser"));
  const accountKey = `account_${user.email}`;
  const account = JSON.parse(localStorage.getItem(accountKey));
  if (!account) {
    mostrarMensaje("accountMessage", "Please create an account first.", "error");
    return;
  }
  const input = prompt("Enter deposit amount:");
  const amount = parseFloat(input);
  if (isNaN(amount) || amount <= 0) {
    mostrarMensaje("accountMessage", "Invalid amount.", "error");
    return;
  }
  account.balance += amount;
  localStorage.setItem(accountKey, JSON.stringify(account));
  mostrarMensaje("accountMessage", `Deposit of $${amount.toFixed(2)} successful.`);
  updateBalanceDisplay();
}

// Simular retiro
function withdraw() {
  const user = JSON.parse(localStorage.getItem("fidexaUser"));
  const accountKey = `account_${user.email}`;
  const account = JSON.parse(localStorage.getItem(accountKey));
  if (!account) {
    mostrarMensaje("accountMessage", "Please create an account first.", "error");
    return;
  }
  const input = prompt("Enter withdrawal amount:");
  const amount = parseFloat(input);
  if (isNaN(amount) || amount <= 0) {
    mostrarMensaje("accountMessage", "Invalid amount.", "error");
    return;
  }
  if (account.balance < amount) {
    mostrarMensaje("accountMessage", "Insufficient funds for withdrawal.", "error");
    return;
  }
  account.balance -= amount;
  localStorage.setItem(accountKey, JSON.stringify(account));
  mostrarMensaje("accountMessage", `Withdrawal of $${amount.toFixed(2)} successful.`);
  updateBalanceDisplay();
}

// Simular transferencia
function transfer() {
  const user = JSON.parse(localStorage.getItem("fidexaUser"));
  const accountKey = `account_${user.email}`;
  const account = JSON.parse(localStorage.getItem(accountKey));
  if (!account) {
    mostrarMensaje("accountMessage", "Please create an account first.", "error");
    return;
  }

  const email = prompt("Enter recipient's email:");
  const amountInput = prompt("Enter transfer amount:");
  const amount = parseFloat(amountInput);

  if (!email || isNaN(amount) || amount <= 0) {
    mostrarMensaje("accountMessage", "Invalid input.", "error");
    return;
  }

  const recipientKey = `account_${email}`;
  const recipientAccount = JSON.parse(localStorage.getItem(recipientKey));

  if (!recipientAccount) {
    mostrarMensaje("accountMessage", "Recipient account not found.", "error");
    return;
  }

  if (account.balance < amount) {
    mostrarMensaje("accountMessage", "Insufficient funds for transfer.", "error");
    return;
  }

  account.balance -= amount;
  recipientAccount.balance += amount;

  localStorage.setItem(accountKey, JSON.stringify(account));
  localStorage.setItem(recipientKey, JSON.stringify(recipientAccount));

  mostrarMensaje("accountMessage", `Transfer of $${amount.toFixed(2)} to ${email} successful.`);
  updateBalanceDisplay();
}

// Eventos para formularios
const loginForm = document.getElementById("loginForm");
if (loginForm) {
  loginForm.addEventListener("submit", function (e) {
    e.preventDefault();
    validateLogin();
  });
}

const registerForm = document.getElementById("registerForm");
if (registerForm) {
  registerForm.addEventListener("submit", function (e) {
    e.preventDefault();
    validateRegistration();
  });
}

// Inicializar vista en account.html
if (window.location.pathname.includes("account.html")) {
  updateBalanceDisplay();
  const user = JSON.parse(localStorage.getItem("fidexaUser"));
  if (user) {
    const welcome = document.getElementById("userWelcome");
    if (welcome) {
      welcome.textContent = `Welcome, ${user.name}!`;
    }
  }
}
