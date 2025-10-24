import React, { useState } from 'react'
import { useAuth } from '../components/Auth/AuthContext'
import { useNotification } from '../components/Notification/NotificationContext'
import { useNavigate } from 'react-router-dom'

export default function LoginPage(){
  const [email,setEmail] = useState('')
  const [password,setPassword] = useState('')
  const { login } = useAuth()
  const { notify } = useNotification()
  const nav = useNavigate()

  async function handle(e){
    e.preventDefault()
    if(!email || !password){ notify('Email y contraseña son obligatorios','error'); return }
    try{
      await login(email,password)
      notify('Sesión iniciada','success')
      nav('/cuentas')
    }catch(e){
      notify(e.message || 'Credenciales inválidas','error')
    }
  }

  return (
    <main className="container">
      <h2>Ingresar</h2>
      <form className="form" onSubmit={handle}>
        <div className="form-row">
          <label>Email</label>
          <input className="input" value={email} onChange={e=>setEmail(e.target.value)} />
        </div>
        <div className="form-row">
          <label>Contraseña</label>
          <input className="input" type="password" value={password} onChange={e=>setPassword(e.target.value)} />
        </div>
        <div style={{display:'flex',gap:8}}>
          <button className="btn" type="submit">Ingresar</button>
        </div>
      </form>
    </main>
  )
}
