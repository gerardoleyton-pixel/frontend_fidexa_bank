import React, { useEffect, useState } from 'react'
import api from '../services/api'

function requireAdmin(){
  return localStorage.getItem('fidexa_admin') === 'true'
}

export default function AdminPage(){
  const [users,setUsers] = useState([])
  const [accounts,setAccounts] = useState([])
  const [transactions,setTransactions] = useState([])
  const [error,setError] = useState(null)

  useEffect(()=>{
    if(!requireAdmin()){ setError('Acceso no autorizado. Inicie sesión como admin.'); return }
    let mounted = true
    async function load(){
      try{
        const u = await api.get('/users')
        const a = await api.get('/accounts')
        const t = await api.get('/transactions')
        if(mounted){ setUsers(u.data || []); setAccounts(a.data || []); setTransactions(t.data || []) }
      }catch(e){ setError(e.message || 'Error al cargar datos') }
    }
    load()
    return ()=> mounted = false
  },[])

  if(error) return <main className="container"><div className="alert error">{error}</div></main>

  return (
    <main className="container">
      <h2>Panel Admin</h2>
      <section className="form">
        <h3>Clientes ({users.length})</h3>
        <table className="table"><thead><tr><th>ID</th><th>Username</th><th>Email</th><th>FullName</th></tr></thead>
        <tbody>{users.map(u=> (<tr key={u.id}><td>{u.id}</td><td>{u.username}</td><td>{u.email}</td><td>{u.fullName}</td></tr>))}</tbody></table>
      </section>

      <section className="form" style={{marginTop:12}}>
        <h3>Cuentas ({accounts.length})</h3>
        <table className="table"><thead><tr><th>ID</th><th>Holder</th><th>Number</th><th>Balance</th><th>User</th></tr></thead>
        <tbody>{accounts.map(a=> (<tr key={a.id}><td>{a.id}</td><td>{a.accountHolder}</td><td>{a.accountNumber}</td><td>${Number(a.balance).toFixed(2)}</td><td>{a.userName}</td></tr>))}</tbody></table>
      </section>

      <section className="form" style={{marginTop:12}}>
        <h3>Transacciones ({transactions.length})</h3>
        <table className="table"><thead><tr><th>ID</th><th>Type</th><th>Amount</th><th>Account</th><th>Desc</th><th>When</th></tr></thead>
        <tbody>{transactions.map(t=> (<tr key={t.id}><td>{t.id}</td><td>{t.type}</td><td>${Number(t.amount).toFixed(2)}</td><td>{t.accountId}</td><td>{t.description}</td><td>{t.timestamp}</td></tr>))}</tbody></table>
      </section>
    </main>
  )
}
