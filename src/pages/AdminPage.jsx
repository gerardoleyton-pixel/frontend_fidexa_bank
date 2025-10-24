import React, { useEffect, useState } from 'react'
import api from '../services/api'
import { useConfirm } from '../components/Confirm/ConfirmContext'
import { useNotification } from '../components/Notification/NotificationContext'

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
  const [searchResult, setSearchResult] = useState(null)
  // modal-specific related data
  const [modalUserAccounts, setModalUserAccounts] = useState([])
  const [modalUserTx, setModalUserTx] = useState([])
  const [editingAccountId, setEditingAccountId] = useState(null)
  const [editingAccountForm, setEditingAccountForm] = useState({ accountHolder:'', accountNumber:'', balance:0 })
  const [modalOpen, setModalOpen] = useState(false)
  const [modalUser, setModalUser] = useState(null)
  const [modalForm, setModalForm] = useState({ fullName:'', username:'', email:'', password:'' })
  const [modalLoading, setModalLoading] = useState(false)
  const [modalError, setModalError] = useState(null)
  const [filterId, setFilterId] = useState('')
  const [filterEmail, setFilterEmail] = useState('')
  const [filterAccNum, setFilterAccNum] = useState('')

  async function openModalForUserId(id){
    if(!id) return
    setModalLoading(true); setModalError(null)
    try{
      const res = await api.get(`/users/${id}`)
      const user = res.data
      setModalUser(user)
      setModalForm({ fullName: user.fullName||'', username: user.username||'', email: user.email||'', password: '' })
      // derive related accounts and transactions from current lists
      const relatedAccounts = accounts.filter(a=> (a.userId && String(a.userId)===String(user.id)) || a.userName===user.username || a.userName===user.email)
      const relatedTx = transactions.filter(t=> relatedAccounts.some(a=> String(a.id)===String(t.accountId)))
      setModalUserAccounts(relatedAccounts)
      setModalUserTx(relatedTx)
      setModalOpen(true)
    }catch(e){ setModalError(e.message || 'Error al cargar usuario') }
    finally{ setModalLoading(false) }
  }

  // confirm/notify helpers (use context if provided, otherwise fallback to window.*)
  const conf = useConfirm()
  const confirmFn = conf && conf.confirm ? conf.confirm : (msg)=> Promise.resolve(window.confirm(msg))
  const notifCtx = useNotification()
  const notify = notifCtx && notifCtx.notify ? notifCtx.notify : (msg, t='info')=> window.alert(msg)

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

      {/* Top filters: id, email, account number (account filter moved to its own row to avoid layout overflow) */}
      <section className="form" style={{marginBottom:12,display:'flex',flexDirection:'column',gap:8}}>
        <div style={{display:'flex',gap:12,alignItems:'center'}}>
          <label style={{minWidth:80}}>Filtrar por ID:</label>
          <input data-testid="filter-id-input" className="input" value={filterId} onChange={e=> setFilterId(e.target.value)} placeholder="ID" style={{width:140}} />
          <button className="btn" onClick={async ()=>{
            const q = (filterId||'').toString().trim()
            if(!q) return
            if(/^\d+$/.test(q)){
              const u = users.find(x=> String(x.id)===q)
              if(u) await openModalForUserId(u?.id)
              else notify('Cliente no encontrado','error')
            } else notify('ID inválido','error')
          }}>Buscar</button>
          <button className="btn" onClick={()=>{ setFilterId('') }}>Limpiar</button>
        </div>

        <div style={{display:'flex',gap:12,alignItems:'center'}}>
          <label style={{minWidth:80}}>Filtrar por email:</label>
          <input data-testid="filter-email-input" className="input" value={filterEmail} placeholder="email@dominio" style={{width:220}} onChange={e=> setFilterEmail(e.target.value)} />
          <button className="btn" onClick={async ()=>{
            const q = (filterEmail||'').toString().trim().toLowerCase()
            if(!q) return
              const u = users.find(x=> (x.email||'').toLowerCase()===q)
              if(u) await openModalForUserId(u.id)
              else notify('Cliente no encontrado','error')
          }}>Buscar</button>
          <button className="btn" onClick={()=>{ setFilterEmail('') }}>Limpiar</button>
        </div>

        <div style={{display:'flex',gap:12,alignItems:'center'}}>
          <label style={{minWidth:160}}>Filtrar por número de cuenta:</label>
          <input data-testid="filter-accnum-input" className="input" value={filterAccNum} placeholder="ACC123" style={{width:220}} onChange={e=> setFilterAccNum(e.target.value)} />
          <button className="btn" onClick={async ()=>{
            const q = (filterAccNum||'').toString().trim()
            if(!q) return
            const acc = accounts.find(a=> (a.accountNumber||'').toLowerCase()===q.toLowerCase())
              if(acc){
                if(acc.userId) await openModalForUserId(acc.userId)
                else{
                  const u = users.find(x=> x.username===acc.userName || x.email===acc.userName)
                  if(u) await openModalForUserId(u.id)
                  else notify('Propietario no encontrado','error')
                }
              } else notify('Cuenta no encontrada','error')
          }}>Buscar</button>
          <button className="btn" onClick={()=>{ setFilterAccNum('') }}>Limpiar</button>
        </div>
      </section>

      <section className="form">
        <div style={{display:'flex',justifyContent:'space-between',alignItems:'center'}}>
          <h3>Clientes ({users.length})</h3>
        </div>
        {/* Removed inline client select filter per UX; selection should be explicit via 'Ver' buttons in tables */}

        <table className="table"><thead><tr><th>ID</th><th>Username</th><th>Email</th><th>FullName</th><th></th></tr></thead>
        <tbody>{users.map(u=> (<tr key={u.id}><td>{u.id}</td><td>{u.username}</td><td>{u.email}</td><td>{u.fullName}</td><td><button className="btn" onClick={async ()=> await openModalForUserId(u.id)}>Ver</button></td></tr>))}</tbody></table>
      </section>

      {/* Modal for user details and editing */}
      {modalOpen && (
        <div style={{position:'fixed',inset:0,background:'rgba(0,0,0,0.4)',display:'flex',justifyContent:'center',alignItems:'center',zIndex:60}} data-testid="user-modal">
          <div style={{background:'#fff',padding:20,width:'90%',maxWidth:900,borderRadius:6,maxHeight:'90%',overflow:'auto'}}>
            <div style={{display:'flex',justifyContent:'space-between',alignItems:'center'}}>
              <h3>Detalles usuario {modalUser?.id || ''}</h3>
              <div>
                <button className="btn" onClick={()=> setModalOpen(false)}>Cerrar</button>
              </div>
            </div>
            {modalLoading && <div className="loading">Cargando...</div>}
            {modalError && <div className="alert error">{modalError}</div>}

            {modalUser && (
              <div>
                <section className="form" style={{marginTop:8}}>
                  <h4>Datos del usuario</h4>
                  <div className="form-row"><label>Nombre completo</label>
                    <input className="input" value={modalForm.fullName} onChange={e=> setModalForm(prev=> ({...prev, fullName: e.target.value}))} /></div>
                  <div className="form-row"><label>Usuario</label>
                    <input className="input" value={modalForm.username} onChange={e=> setModalForm(prev=> ({...prev, username: e.target.value}))} /></div>
                  <div className="form-row"><label>Email</label>
                    <input className="input" value={modalForm.email} onChange={e=> setModalForm(prev=> ({...prev, email: e.target.value}))} /></div>
                  <div className="form-row"><label>Contraseña (dejar vacío para no cambiar)</label>
                    <input className="input" type="password" value={modalForm.password} onChange={e=> setModalForm(prev=> ({...prev, password: e.target.value}))} /></div>
                  <div style={{marginTop:8}}>
                    <button className="btn" onClick={async ()=>{
                      setModalLoading(true); setModalError(null)
                      try{
                        const payload = { fullName: modalForm.fullName, username: modalForm.username, email: modalForm.email }
                        if(modalForm.password) payload.password = modalForm.password
                        await api.put(`/users/${modalUser.id}`, payload)
                        // refresh users list
                        const u = await api.get('/users'); setUsers(u.data || [])
                        // update modal user
                        const updated = await api.get(`/users/${modalUser.id}`)
                        setModalUser(updated.data)
                        setSearchResult(prev=> prev && prev.type==='user' ? ({...prev, user: updated.data}) : prev)
                      }catch(e){ setModalError(e.message || 'Error al actualizar usuario') }
                      finally{ setModalLoading(false) }
                    }}>Guardar cambios</button>
                    <button className="btn" style={{marginLeft:8,background:'#c33',color:'#fff'}} onClick={async ()=>{
                      {
                        const ok = await confirmFn('Eliminar usuario y todos sus datos? Esta acción es irreversible.')
                        if(!ok) return
                        setModalLoading(true); setModalError(null)
                        try{
                          await api.delete(`/users/${modalUser.id}`)
                          // remove from users list
                          setUsers(prev=> prev.filter(x=> String(x.id)!==String(modalUser.id)))
                          // remove any accounts belonging to user
                          setAccounts(prev=> prev.filter(a=> String(a.userId)!==String(modalUser.id)))
                          setModalOpen(false)
                          notify('Usuario eliminado','success')
                        }catch(e){ setModalError(e.message || 'Error al eliminar usuario') }
                        finally{ setModalLoading(false) }
                      }
                    }}>Eliminar usuario</button>
                  </div>
                </section>

                {/* Client filter modal (open when 'Actualizar clientes' clicked) */}
                  {/* Client filter modal removed per UX request */}

                <section className="form" style={{marginTop:12}}>
                  <h4>Cuentas del usuario</h4>
                  <table className="table"><thead><tr><th>ID</th><th>Holder</th><th>Number</th><th>Balance</th><th></th></tr></thead>
                    <tbody>{(modalUserAccounts||[]).map(a=> (
                      <tr key={a.id}>
                        <td>{a.id}</td>
                        <td>
                          {editingAccountId===a.id ? (
                            <input className="input" value={editingAccountForm.accountHolder} onChange={e=> setEditingAccountForm(prev=> ({...prev, accountHolder: e.target.value}))} />
                          ) : a.accountHolder}
                        </td>
                        <td>
                          {editingAccountId===a.id ? (
                            <input className="input" value={editingAccountForm.accountNumber} onChange={e=> setEditingAccountForm(prev=> ({...prev, accountNumber: e.target.value}))} />
                          ) : a.accountNumber}
                        </td>
                        <td>
                          {editingAccountId===a.id ? (
                            <input className="input" value={editingAccountForm.balance} onChange={e=> setEditingAccountForm(prev=> ({...prev, balance: e.target.value}))} />
                          ) : (`$${Number(a.balance).toFixed(2)}`)}
                        </td>
                        <td style={{whiteSpace:'nowrap'}}>
                          {editingAccountId===a.id ? (
                            <>
                              <button className="btn" onClick={async ()=>{
                                // save account changes
                                const payload = { accountHolder: editingAccountForm.accountHolder, accountNumber: editingAccountForm.accountNumber, balance: Number(editingAccountForm.balance) }
                                try{
                                  // validation: accountNumber required, balance numeric
                                  if(!editingAccountForm.accountNumber || editingAccountForm.accountNumber.toString().trim()===''){
                                    notify('Número de cuenta es obligatorio','error'); return
                                  }
                                  const bal = Number(editingAccountForm.balance)
                                  if(Number.isNaN(bal)) { notify('Saldo inválido','error'); return }
                                  await api.put(`/accounts/${a.id}`, payload)
                                  // refresh accounts list
                                  const res = await api.get('/accounts'); setAccounts(res.data || [])
                                  // refresh modal accounts
                                  const updatedAcc = (res.data||[]).find(x=> String(x.id)===String(a.id))
                                  setModalUserAccounts(prev=> prev.map(p=> p.id===a.id ? (updatedAcc||{...p,...payload}) : p))
                                  setEditingAccountId(null)
                                  notify('Cuenta actualizada','success')
                                }catch(e){ notify(e.message || 'Error al actualizar cuenta','error') }
                              }}>Guardar</button>
                              <button className="btn" style={{marginLeft:8}} onClick={()=> setEditingAccountId(null)}>Cancelar</button>
                            </>
                          ) : (
                            <>
                              <button className="btn" onClick={()=>{
                                setEditingAccountId(a.id)
                                setEditingAccountForm({ accountHolder: a.accountHolder||'', accountNumber: a.accountNumber||'', balance: a.balance||0 })
                              }}>Editar</button>
                              <button className="btn" style={{marginLeft:8}} onClick={async ()=>{
                                {
                                  const ok = await confirmFn('Eliminar esta cuenta? Esta acción es irreversible.')
                                  if(!ok) return
                                  try{
                                    await api.delete(`/accounts/${a.id}`)
                                    // remove from lists
                                    setAccounts(prev=> prev.filter(x=> String(x.id)!==String(a.id)))
                                    setModalUserAccounts(prev=> prev.filter(x=> String(x.id)!==String(a.id)))
                                    notify('Cuenta eliminada','success')
                                  }catch(e){ notify(e.message || 'Error al eliminar cuenta','error') }
                                }
                              }}>Eliminar</button>
                            </>
                          )}
                        </td>
                      </tr>
                    ))}</tbody>
                  </table>
                </section>

                <section className="form" style={{marginTop:12}}>
                  <h4>Transacciones relacionadas</h4>
                  <table className="table"><thead><tr><th>ID</th><th>Type</th><th>Amount</th><th>Account</th></tr></thead>
                    <tbody>{(modalUserTx||[]).map(t=> (<tr key={t.id}><td>{t.id}</td><td>{t.type}</td><td>${Number(t.amount).toFixed(2)}</td><td>{t.accountId}</td></tr>))}</tbody>
                  </table>
                </section>
              </div>
            )}
          </div>
        </div>
      )}

      <section className="form" style={{marginTop:12}}>
        <div style={{display:'flex',justifyContent:'space-between',alignItems:'center'}}>
          <h3>Cuentas ({accounts.length})</h3>
          <div style={{display:'flex',gap:8,alignItems:'center'}}>
            <label htmlFor="account-select">Seleccionar cuenta:</label>
            {/* Removed inline account select and id search per UX. Use table 'Ver' buttons to inspect accounts. */}
          </div>
        </div>

        <table className="table"><thead><tr><th>ID</th><th>Holder</th><th>Number</th><th>Balance</th><th>User</th><th></th></tr></thead>
        <tbody>{accounts.map(a=> (
          <tr key={a.id}>
            <td>{a.id}</td><td>{a.accountHolder}</td><td>{a.accountNumber}</td><td>${Number(a.balance).toFixed(2)}</td><td>{a.userName}</td>
            <td><button className="btn" onClick={async ()=>{
              // explicit: open modal for account owner
              if(a.userId) await openModalForUserId(a.userId)
              else{
                const u = users.find(x=> x.username===a.userName || x.email===a.userName)
                if(u) await openModalForUserId(u.id)
                else notify('Propietario no encontrado','error')
              }
            }}>Ver</button></td>
          </tr>
        ))}</tbody></table>
        {/* Removed inline per-user accounts view (use modal 'Ver' for details) */}
      </section>

      <section className="form" style={{marginTop:12}}>
        <div style={{display:'flex',justifyContent:'space-between',alignItems:'center'}}>
          <h3>Transacciones ({transactions.length})</h3>
          <div style={{display:'flex',gap:8,alignItems:'center'}}>
            <span style={{color:'#666'}}>Usa los botones 'Ver' en la tabla para abrir detalles.</span>
          </div>
        </div>

        <table className="table"><thead><tr><th>ID</th><th>Type</th><th>Amount</th><th>Account</th><th>Desc</th><th>When</th></tr></thead>
        <tbody>{transactions.map(t=> (<tr key={t.id}><td>{t.id}</td><td>{t.type}</td><td>${Number(t.amount).toFixed(2)}</td><td>{t.accountId}</td><td>{t.description}</td><td>{t.timestamp}</td></tr>))}</tbody></table>
        {/* Removed per-user transaction subsection to keep filters centralized in top area */}
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
          {/* 'Actualizar datos' removed from account detail per UX */}
        </section>
      )}
    </main>
  )
}
