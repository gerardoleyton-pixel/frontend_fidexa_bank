import React, { useEffect, useState } from 'react'
import api from '../services/api'

function requireAdmin(){
  return localStorage.getItem('fidexa_admin') === 'true'
}

export default function AdminPage(){
  const [users,setUsers] = useState([])
  const [accounts,setAccounts] = useState([])
  const [transactions,setTransactions] = useState([])
  const [selectedAccount,setSelectedAccount] = useState(null)
  const [selectedUser,setSelectedUser] = useState(null)
  const [userAccounts,setUserAccounts] = useState([])
  const [userTransactions,setUserTransactions] = useState([])
  const [selectedAccountTransactions,setSelectedAccountTransactions] = useState([])
  const [selectedTransaction,setSelectedTransaction] = useState(null)
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
        <div style={{display:'flex',justifyContent:'space-between',alignItems:'center'}}>
          <h3>Clientes ({users.length})</h3>
          <div>
            <button className="btn" onClick={async ()=>{
              setError(null)
              try{ const u = await api.get('/users'); setUsers(u.data || []) }catch(e){ setError('Error al refrescar clientes') }
            }}>Actualizar clientes</button>
          </div>
        </div>
        <div style={{display:'flex',gap:8,alignItems:'center',marginBottom:8}}>
          <label htmlFor="user-select">Seleccionar cliente:</label>
          <select id="user-select" value={selectedUser?.id || ''} onChange={e=>{
            const id = e.target.value
            const u = users.find(x=>String(x.id)===String(id))
            setSelectedUser(u || null)
            if(u){
              // derive user accounts from accounts list
              const ua = accounts.filter(a => (a.userId && String(a.userId)===String(u.id)) || (a.userName && (a.userName===u.username || a.userName===u.email || a.userName===u.fullName)))
              setUserAccounts(ua)
              // derive user transactions from transactions list
              const ut = transactions.filter(t => ua.some(acc => String(acc.id)===String(t.accountId)))
              setUserTransactions(ut)
            } else { setUserAccounts([]); setUserTransactions([]) }
          }}>
            <option value="">-- seleccionar --</option>
            {users.map(u=> (<option key={u.id} value={u.id}>{u.fullName || u.username || u.email} (ID:{u.id})</option>))}
          </select>
          <button className="btn" onClick={()=>{ setSelectedUser(null); setUserAccounts([]); setUserTransactions([]); document.getElementById('user-select').value=''; }}>Limpiar</button>
        </div>

        <table className="table"><thead><tr><th>ID</th><th>Username</th><th>Email</th><th>FullName</th></tr></thead>
        <tbody>{users.map(u=> (<tr key={u.id}><td>{u.id}</td><td>{u.username}</td><td>{u.email}</td><td>{u.fullName}</td></tr>))}</tbody></table>
      </section>

      <section className="form" style={{marginTop:12}}>
        <div style={{display:'flex',justifyContent:'space-between',alignItems:'center'}}>
          <h3>Cuentas ({accounts.length})</h3>
          <div style={{display:'flex',gap:8,alignItems:'center'}}>
            <label htmlFor="account-select">Seleccionar cuenta:</label>
            <select id="account-select" value={selectedAccount?.id || ''} onChange={async e=>{
              const id = e.target.value
              if(!id){ setSelectedAccount(null); setSelectedAccountTransactions([]); return }
              try{
                // try fetching by id (preferred)
                const res = await api.get(`/accounts/${id}`)
                setSelectedAccount(res.data)
                // transactions for this account
                const sat = transactions.filter(t => String(t.accountId)===String(id))
                setSelectedAccountTransactions(sat)
              }catch(err){
                // fallback to local lookup
                const local = accounts.find(a=>String(a.id)===String(id))
                setSelectedAccount(local || null)
                const sat = transactions.filter(t => local && String(t.accountId)===String(local.id))
                setSelectedAccountTransactions(sat)
              }
            }}>
              <option value="">-- seleccionar --</option>
              {accounts.map(a=> (<option key={a.id} value={a.id}>{a.accountHolder} - {a.accountNumber} (ID:{a.id})</option>))}
            </select>
            <input className="input" placeholder="Buscar por ID" id="account-id-input" style={{width:140}} />
            <button className="btn" onClick={async ()=>{
              const id = document.getElementById('account-id-input').value.trim()
              if(!id){ setError('Ingrese un ID de cuenta'); return }
              try{
                const res = await api.get(`/accounts/${id}`)
                setSelectedAccount(res.data)
                const sat = transactions.filter(t => String(t.accountId)===String(id))
                setSelectedAccountTransactions(sat)
              }catch(e){ setError('Cuenta no encontrada') }
            }}>Buscar</button>
            <button className="btn" onClick={async ()=>{
              setError(null)
              try{ const a = await api.get('/accounts'); setAccounts(a.data || []) }catch(e){ setError('Error al refrescar cuentas') }
            }}>Actualizar cuentas</button>
          </div>
        </div>

        <table className="table"><thead><tr><th>ID</th><th>Holder</th><th>Number</th><th>Balance</th><th>User</th></tr></thead>
        <tbody>{accounts.map(a=> (
          <tr key={a.id} style={{cursor:'pointer'}} onClick={()=> setSelectedAccount(a)}>
            <td>{a.id}</td><td>{a.accountHolder}</td><td>{a.accountNumber}</td><td>${Number(a.balance).toFixed(2)}</td><td>{a.userName}</td>
          </tr>
        ))}</tbody></table>
        {/* if a user is selected, show their accounts */}
        {selectedUser && (
          <div style={{marginTop:10}}>
            <h4>Cuentas del cliente {selectedUser.fullName || selectedUser.username}</h4>
            <table className="table"><thead><tr><th>ID</th><th>Holder</th><th>Number</th><th>Balance</th></tr></thead>
            <tbody>{userAccounts.map(a=> (<tr key={a.id} style={{cursor:'pointer'}} onClick={()=> setSelectedAccount(a)}><td>{a.id}</td><td>{a.accountHolder}</td><td>{a.accountNumber}</td><td>${Number(a.balance).toFixed(2)}</td></tr>))}</tbody></table>
          </div>
        )}
      </section>

      <section className="form" style={{marginTop:12}}>
        <div style={{display:'flex',justifyContent:'space-between',alignItems:'center'}}>
          <h3>Transacciones ({transactions.length})</h3>
          <div style={{display:'flex',gap:8,alignItems:'center'}}>
            <label htmlFor="tx-select">Filtrar por transacción:</label>
            <select id="tx-select" value={selectedTransaction?.id || ''} onChange={e=>{
              const id = e.target.value
              const tx = transactions.find(x=>String(x.id)===String(id))
              setSelectedTransaction(tx || null)
            }}>
              <option value="">-- seleccionar --</option>
              {transactions.map(tx=> (<option key={tx.id} value={tx.id}>{tx.type} - ${Number(tx.amount).toFixed(2)} (ID:{tx.id})</option>))}
            </select>
            <button className="btn" onClick={async ()=>{
              setError(null)
              try{ const t = await api.get('/transactions'); setTransactions(t.data || []) }catch(e){ setError('Error al refrescar transacciones') }
            }}>Actualizar transacciones</button>
          </div>
        </div>

        <table className="table"><thead><tr><th>ID</th><th>Type</th><th>Amount</th><th>Account</th><th>Desc</th><th>When</th></tr></thead>
        <tbody>{transactions.map(t=> (<tr key={t.id}><td>{t.id}</td><td>{t.type}</td><td>${Number(t.amount).toFixed(2)}</td><td>{t.accountId}</td><td>{t.description}</td><td>{t.timestamp}</td></tr>))}</tbody></table>
        {/* if a user is selected show their transactions */}
        {selectedUser && (
          <div style={{marginTop:10}}>
            <h4>Transacciones del cliente {selectedUser.fullName || selectedUser.username}</h4>
            <table className="table"><thead><tr><th>ID</th><th>Type</th><th>Amount</th><th>Account</th><th>When</th></tr></thead>
            <tbody>{userTransactions.map(tx=> (<tr key={tx.id}><td>{tx.id}</td><td>{tx.type}</td><td>${Number(tx.amount).toFixed(2)}</td><td>{tx.accountId}</td><td>{tx.timestamp}</td></tr>))}</tbody></table>
          </div>
        )}
      </section>

      {/* Selected account detail */}
      {selectedAccount && (
        <section className="form" style={{marginTop:12}}>
          <h3>Detalle cuenta: {selectedAccount.id}</h3>
          <div style={{display:'flex',gap:12,flexWrap:'wrap'}}>
            <div><strong>Titular:</strong> {selectedAccount.accountHolder}</div>
            <div><strong>Número:</strong> {selectedAccount.accountNumber}</div>
            <div><strong>Saldo:</strong> ${Number(selectedAccount.balance).toFixed(2)}</div>
            <div><strong>Usuario:</strong> {selectedAccount.userName}</div>
          </div>
          <div style={{marginTop:10}}>
            <button className="btn" onClick={async ()=>{
              // refresh this account
              try{
                const res = await api.get(`/accounts/${selectedAccount.id}`)
                setSelectedAccount(res.data)
                // also update accounts list
                const a = await api.get('/accounts'); setAccounts(a.data || [])
              }catch(e){ setError('Error al actualizar la cuenta') }
            }}>Actualizar datos</button>
          </div>
        </section>
      )}
    </main>
  )
}
