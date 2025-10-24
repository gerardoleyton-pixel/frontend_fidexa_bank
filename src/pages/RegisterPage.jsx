import React, { useState } from 'react'
import api from '../services/api'
import { useNotification } from '../components/Notification/NotificationContext'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../components/Auth/AuthContext'

export default function RegisterPage(){
  const [username,setUsername] = useState('')
  const [email,setEmail] = useState('')
  const [password,setPassword] = useState('')
  const [fullName,setFullName] = useState('')
  const [submitting,setSubmitting] = useState(false)
  const { notify } = useNotification()
  const nav = useNavigate()
  const { login } = useAuth()

  async function handle(e){
    e.preventDefault()
    if(!username||!email||!password||!fullName){ notify('Todos los campos son obligatorios','error'); return }
    setSubmitting(true)
    try{
      await api.post('/users', { username, email, password, fullName })
      // auto-login after successful registration
      try{
        await login(email, password)
        notify('Registro exitoso. Sesión iniciada.','success')
        nav('/cuentas')
        return
      }catch(err){
        // fallback: navigate to login page
        notify('Registro exitoso. Por favor inicie sesión.','success')
        nav('/login')
        return
      }
    }catch(err){
      notify(err.message || 'Error al registrar','error')
    }finally{setSubmitting(false)}
  }

  return (
    <main className="container">
      <h2>Registro</h2>
      <form className="form" onSubmit={handle}>
        <div className="form-row"><label>Nombre completo</label><input className="input" value={fullName} onChange={e=>setFullName(e.target.value)} /></div>
        <div className="form-row"><label>Usuario</label><input className="input" value={username} onChange={e=>setUsername(e.target.value)} /></div>
        <div className="form-row"><label>Email</label><input className="input" value={email} onChange={e=>setEmail(e.target.value)} /></div>
        <div className="form-row"><label>Contraseña</label><input className="input" type="password" value={password} onChange={e=>setPassword(e.target.value)} /></div>
        <div style={{display:'flex',gap:8}}>
          <button className="btn" type="submit" disabled={submitting}>{submitting? 'Registrando...':'Registrar'}</button>
        </div>
      </form>
    </main>
  )
}
