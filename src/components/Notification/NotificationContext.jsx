import React, { createContext, useState, useContext } from 'react'

const NotificationContext = createContext()

function Modal({ title = 'Mensaje', message, type = 'success', onClose }){
  return (
    <div style={{position:'fixed',inset:0,display:'flex',alignItems:'center',justifyContent:'center',zIndex:1200}}>
      <div style={{position:'absolute',inset:0,background:'rgba(0,0,0,0.4)'}} onClick={onClose} />
      <div style={{background:'white',padding:20,borderRadius:10,boxShadow:'0 8px 24px rgba(0,0,0,0.2)',zIndex:1201,maxWidth:480,width:'90%'}}>
        <h3 style={{marginTop:0}}>{title}</h3>
        <div style={{marginBottom:16,color: type === 'error' ? '#8a1f1f' : '#046a4b'}}>{message}</div>
        <div style={{textAlign:'right'}}>
          <button className="btn" onClick={onClose}>OK</button>
        </div>
      </div>
    </div>
  )
}

export function NotificationProvider({ children }) {
  const [modal, setModal] = useState(null)

  function notify(msg, t = 'success') {
    setModal({ message: msg, type: t })
  }

  function close() { setModal(null) }

  return (
    <NotificationContext.Provider value={{ notify }}>
      {children}
      {modal && (
        <Modal title={modal.type === 'error' ? 'Error' : 'Información'} message={modal.message} type={modal.type} onClose={close} />
      )}
    </NotificationContext.Provider>
  )
}

export function useNotification(){
  const ctx = useContext(NotificationContext)
  return ctx
}
