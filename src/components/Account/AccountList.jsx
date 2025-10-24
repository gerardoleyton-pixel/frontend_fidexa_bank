import React, { useEffect, useState } from 'react'
import accountsService from '../../services/accounts'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../Auth/AuthContext'
import { useNotification } from '../Notification/NotificationContext'

export default function AccountList(){
  const [accounts, setAccounts] = useState([])
  const [loading, setLoading] = useState(true)
  const { user } = useAuth()
  const { notify } = useNotification()
  const nav = useNavigate()

  useEffect(()=>{
    let mounted = true
    async function load(){
      setLoading(true)
      try{
        if(!user){
          // don't load public accounts; require login to view accounts
          if(mounted) setAccounts([])
        } else {
          const data = accountsService.getByUser ? await accountsService.getByUser(user.id) : await accountsService.getAll()
          if(mounted) setAccounts(data)
        }
      }catch(e){
        notify(e.message || 'Error al cargar cuentas', 'error')
      }finally{ if(mounted) setLoading(false) }
    }
    load()
    return ()=> mounted = false
  },[user])

  if(loading) return <div className="loading">Cargando cuentas...</div>

  return (
    <div>
      <div style={{display:'flex',justifyContent:'space-between',alignItems:'center',marginBottom:12}}>
        <h2>Listado de cuentas</h2>
        <button className="btn" onClick={()=>nav('/cuentas/nueva')}>Registrar nueva cuenta</button>
      </div>

      {accounts.length === 0 ? (
        <div className="form">No hay cuentas registradas.</div>
      ) : (
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Titular</th>
              <th>Número</th>
              <th>Saldo</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {accounts.map(c => (
              <tr key={c.id}>
                <td>{c.id}</td>
                <td>{c.accountHolder}</td>
                <td>{c.accountNumber}</td>
                <td>${Number(c.balance).toFixed(2)}</td>
                <td style={{padding:'8px'}}>
                  <div className="flex-wrap">
                    <button className="btn" onClick={()=>nav(`/cuentas/${c.id}`)}>Ver</button>
                    <button className="btn" onClick={()=>nav(`/transacciones/cuenta/${c.id}/deposito`)}>Depositar</button>
                    <button className="btn" onClick={()=>nav(`/transacciones/cuenta/${c.id}/retiro`)}>Retirar</button>
                    <button className="btn" onClick={()=>nav(`/transacciones/cuenta/${c.id}/transferencia`)}>Transferir</button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

