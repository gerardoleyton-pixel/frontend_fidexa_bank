import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../Auth/AuthContext'
import { useConfirm } from '../Confirm/ConfirmContext'

export default function Header(){
  const { user, logout } = useAuth()
  const nav = useNavigate()

  function doLogout(){
    logout()
    nav('/')
  }

  const { confirm } = useConfirm()

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
              <button className="btn" onClick={doLogout}>Salir</button>
            </div>
          ) : (
            /* Make Ingresar use the same primary styling as Registro */
            <button className="nav-button nav-cta" onClick={()=>nav('/login')}>Ingresar</button>
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
