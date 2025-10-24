import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../Auth/AuthContext'
import { useConfirm } from '../Confirm/ConfirmContext'
import api from '../../services/api'
import { useNotification } from '../Notification/NotificationContext'

export default function Header(){
  const { user, logout, updateUser } = useAuth()
  const nav = useNavigate()
  const notifCtx = useNotification()
  const notify = notifCtx && notifCtx.notify ? notifCtx.notify : (()=>{})

  // profile edit modal state
  const [profileOpen, setProfileOpen] = useState(false)
  const [profileForm, setProfileForm] = useState({ fullName:'', username:'', email:'', password:'' })
  const [profileLoading, setProfileLoading] = useState(false)

  function doLogout(){
    logout()
    nav('/')
  }

  const confirmCtx = useConfirm()
  const confirm = confirmCtx && confirmCtx.confirm ? confirmCtx.confirm : (msg)=> Promise.resolve(window.confirm(msg))

  const isAdmin = localStorage.getItem('fidexa_admin') === 'true'

  return (
    <header className="header container">
      <div className="header-left" style={{display:'flex',alignItems:'center',gap:12}}>
        <img src="/images/logo.png" alt="Fidexa" className="header-logo" onError={(e)=>e.target.style.display='none'} />
      </div>
      <nav className="header-nav">
        {/* left group: Registro / Ingresar / user actions / admin */}
        <div className="nav-left">
          {!user && <button className="nav-button nav-cta" onClick={()=>nav('/register')}>Registro</button>}

          {user ? (
            <div className="nav-user">
              <span className="user-name">{user.fullName || user.username || user.email}</span>
              <button className="btn" onClick={()=> { setProfileForm({ fullName: user.fullName||'', username: user.username||'', email: user.email||'', password: '' }); setProfileOpen(true) }}>Actualizar datos</button>
              <button className="btn" onClick={doLogout}>Salir</button>
            </div>
          ) : (
            /* If admin flag is set but no user object, show a logout for admin */
            (isAdmin ? (
              <button className="nav-button nav-cta" onClick={()=>{ localStorage.removeItem('fidexa_admin'); nav('/'); }}>Cerrar sesión</button>
            ) : (
              /* Make Ingresar use the same primary styling as Registro */
              <button className="nav-button nav-cta" onClick={()=>nav('/login')}>Ingresar</button>
            ))
          )}

          </div>

          {/* right group: Home (casita) placed at the far right so it is not between Registro and Ingresar */}
          <div className="nav-right">
            <HomeButton user={user} confirm={confirm} logout={logout} nav={nav} />
            {isAdmin && (
              /* crown must be to the right of the casita */
              <AdminButton onClick={()=>nav('/admin')} />
            )}
          </div>
      </nav>

      {/* Profile modal for user to edit own data */}
      {profileOpen && (
        <div style={{position:'fixed',inset:0,background:'rgba(0,0,0,0.4)',display:'flex',justifyContent:'center',alignItems:'center',zIndex:60}} data-testid="profile-modal">
          <div style={{background:'#fff',padding:20,width:'90%',maxWidth:560,borderRadius:6}}>
            <div style={{display:'flex',justifyContent:'space-between',alignItems:'center'}}>
              <h3>Actualizar perfil</h3>
              <div><button className="btn" onClick={()=> setProfileOpen(false)}>Cerrar</button></div>
            </div>
            {profileLoading && <div className="loading">Guardando...</div>}
            <div style={{marginTop:8}}>
              <div className="form-row"><label>Nombre completo</label>
                <input className="input" value={profileForm.fullName} onChange={e=> setProfileForm(p=> ({...p, fullName: e.target.value}))} /></div>
              <div className="form-row"><label>Usuario</label>
                <input className="input" value={profileForm.username} onChange={e=> setProfileForm(p=> ({...p, username: e.target.value}))} /></div>
              <div className="form-row"><label>Email</label>
                <input className="input" value={profileForm.email} onChange={e=> setProfileForm(p=> ({...p, email: e.target.value}))} /></div>
              <div className="form-row"><label>Contraseña (dejar vacío para no cambiar)</label>
                <input className="input" type="password" value={profileForm.password} onChange={e=> setProfileForm(p=> ({...p, password: e.target.value}))} /></div>
              <div style={{marginTop:8,display:'flex',gap:8,justifyContent:'flex-end'}}>
                <button className="btn" onClick={async ()=>{
                  if(!user) return notify('No hay usuario logueado','error')
                  // validations
                  if(!profileForm.username || profileForm.username.toString().trim()==='') { notify('Usuario es obligatorio','error'); return }
                  const email = (profileForm.email||'').toString().trim()
                  const emailRe = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
                  if(!emailRe.test(email)) { notify('Email inválido','error'); return }
                  if(profileForm.password && profileForm.password.length>0 && profileForm.password.length < 6){ notify('La contraseña debe tener al menos 6 caracteres','error'); return }
                  setProfileLoading(true)
                  try{
                    const payload = { fullName: profileForm.fullName, username: profileForm.username, email: profileForm.email }
                    if(profileForm.password) payload.password = profileForm.password
                    const res = await api.put(`/users/${user.id}`, payload)
                    // update context and localStorage
                    const updated = res.data || { ...user, ...payload }
                    updateUser(updated)
                    notify('Perfil actualizado','success')
                    setProfileOpen(false)
                  }catch(e){ notify(e.message || 'Error al actualizar perfil','error') }
                  finally{ setProfileLoading(false) }
                }}>Guardar</button>
                <button className="btn" onClick={()=> setProfileOpen(false)}>Cancelar</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </header>
  )
}

function HomeButton({ user, confirm, logout, nav }){
  async function onClick(ev){
    ev.preventDefault()
    const isAdmin = localStorage.getItem('fidexa_admin') === 'true'
    if(user || isAdmin){
      const ok = await confirm('Al pulsar la casita se cerrará la sesión. ¿Deseas continuar?', 'Cerrar sesión?')
      if(ok){
        if(user) logout()
        if(isAdmin) localStorage.removeItem('fidexa_admin')
        nav('/')
      }
    } else { nav('/') }
  }
  return (
    <button className="home-button nav-button" aria-label="Ir al inicio" onClick={onClick}>
      {/* house svg */}
      <svg className="home-svg" width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden>
        <path d="M3 10.5L12 3l9 7.5V21a1 1 0 0 1-1 1h-5v-7H9v7H4a1 1 0 0 1-1-1V10.5z" fill="#0A2540"/>
      </svg>
    </button>
  )
}

function AdminButton({ onClick }){
  const [imgLoaded, setImgLoaded] = useState(false)
  return (
    <button className="nav-button" title="Admin" onClick={onClick} style={{display:'flex',alignItems:'center',gap:6}}>
      <img src="/images/crown.svg" alt="Admin" className="admin-icon" onLoad={()=>setImgLoaded(true)} onError={(e)=>{e.target.style.display='none'; setImgLoaded(false)}} style={{display: imgLoaded ? 'inline-block' : 'none', width:20, height:20}} />
      {!imgLoaded && (
        <svg className="admin-svg" width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden>
          <path d="M5 16L3 6l4 3 4-6 4 6 4-3-2 10H5z" fill="#FFC107" stroke="#E0A800" strokeWidth="0.5"/>
        </svg>
      )}
    </button>
  )
}
