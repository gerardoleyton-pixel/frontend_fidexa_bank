import React, { useEffect, useState } from 'react'
import transactionsService from '../../services/transactions'
import { useNotification } from '../Notification/NotificationContext'
import { useNavigate } from 'react-router-dom'

export default function TransactionList({ accountId }){
  const [movimientos, setMovimientos] = useState([])
  const [loading, setLoading] = useState(true)
  const { notify } = useNotification()
  const nav = useNavigate()

  useEffect(()=>{
    let mounted = true
    async function load(){
      setLoading(true)
      try{
        const data = await transactionsService.listByAccount(accountId)
        if(mounted) setMovimientos(data)
      }catch(e){
        notify(e.message || 'Error cargando movimientos','error')
      }finally{ if(mounted) setLoading(false) }
    }
    load()
    return ()=> mounted = false
  },[accountId])

  if(loading) return <div className="loading">Cargando movimientos...</div>

  return (
    <div>
      <h3 style={{display:'flex',justifyContent:'space-between',alignItems:'center'}}>Movimientos</h3>
      {movimientos.length === 0 ? (
    <div className="form">No hay transacciones registradas.</div>
      ) : (
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Tipo</th>
              <th>Monto</th>
              <th>Fecha</th>
            </tr>
          </thead>
          <tbody>
            {movimientos.map(tx=> (
              <tr key={tx.id}>
                <td>{tx.id}</td>
                <td>{tx.type}</td>
                <td>${Number(tx.amount).toFixed(2)}</td>
                <td>{new Date(tx.timestamp).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      <div className="actions" style={{marginTop:12,display:'flex',gap:10}}>
        <button className="btn" onClick={()=>nav(`/transacciones/cuenta/${accountId}/deposito`)}>Depositar</button>
        <button className="btn" onClick={()=>nav(`/transacciones/cuenta/${accountId}/retiro`)}>Retirar</button>
        <button className="btn" onClick={()=>nav(`/transacciones/cuenta/${accountId}/transferencia`)}>Transferir</button>
      </div>
    </div>
  )
}
