declare const axios: any;

interface BankAccount {
  id: number;
  accountNumber: string;
  userId: number;
}

document.addEventListener("DOMContentLoaded", () => {
  const userIdInput = document.getElementById("usuarioId") as HTMLInputElement;
  const accountNumberInput = document.getElementById("numeroCuenta") as HTMLInputElement;
  const feedback = document.getElementById("feedback") as HTMLDivElement;

  async function checkUserHasAccounts(userId: number): Promise<void> {
    try {
      const response = await axios.get(`/accounts/by-user?userId=${userId}`);
      if (Array.isArray(response.data) && response.data.length > 0) {
        feedback.innerText = "⚠️ Este usuario ya tiene una cuenta registrada.";
        feedback.style.color = "orange";
      } else {
        feedback.innerText = "";
      }
    } catch (error: any) {
      feedback.innerText = "❌ Error al verificar cuentas del usuario.";
      feedback.style.color = "red";
    }
  }

  async function checkAccountNumberExists(accountNumber: string): Promise<void> {
    try {
      const response = await axios.get("/accounts");
      const exists = Array.isArray(response.data) &&
        response.data.some((c: BankAccount) => c.accountNumber === accountNumber);

      if (exists) {
        feedback.innerText = "⚠️ Este número de cuenta ya está en uso.";
        feedback.style.color = "orange";
      } else {
        feedback.innerText = "";
      }
    } catch (error: any) {
      feedback.innerText = "❌ Error al verificar número de cuenta.";
      feedback.style.color = "red";
    }
  }

  userIdInput.addEventListener("blur", () => {
    const userId = parseInt(userIdInput.value);
    if (!isNaN(userId)) {
      checkUserHasAccounts(userId);
    }
  });

  accountNumberInput.addEventListener("blur", () => {
    const accountNumber = accountNumberInput.value.trim();
    if (accountNumber) {
      checkAccountNumberExists(accountNumber);
    }
  });
});
