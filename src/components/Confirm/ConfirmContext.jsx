import React, { createContext, useContext, useState } from 'react'

const ConfirmContext = createContext()

function ConfirmModal({ title='Confirmar', message, onCancel, onConfirm }){
  return (
    <div style={{position:'fixed',inset:0,display:'flex',alignItems:'center',justifyContent:'center',zIndex:1300}}>
      <div style={{position:'absolute',inset:0,background:'rgba(0,0,0,0.4)'}} onClick={onCancel} />
      <div style={{background:'white',padding:20,borderRadius:8,boxShadow:'0 8px 24px rgba(0,0,0,0.2)',zIndex:1301,maxWidth:420,width:'90%'}}>
        <h3 style={{marginTop:0}}>{title}</h3>
        <div style={{marginBottom:16}}>{message}</div>
        <div style={{display:'flex',justifyContent:'flex-end',gap:8}}>
          <button className="nav-button" onClick={onCancel}>Cancelar</button>
          <button className="btn" onClick={onConfirm}>Confirmar</button>
        </div>
      </div>
    </div>
  )
}

export function ConfirmProvider({ children }){
  const [modal, setModal] = useState(null)

  function confirm(message, title='Confirmar'){ 
    return new Promise((resolve)=>{
      setModal({ message, title, resolve })
    })
  }

  function handleCancel(){
    if(modal && modal.resolve) modal.resolve(false)
    setModal(null)
  }

  function handleConfirm(){
    if(modal && modal.resolve) modal.resolve(true)
    setModal(null)
  }

  return (
    <ConfirmContext.Provider value={{ confirm }}>
      {children}
      {modal && (
        <ConfirmModal title={modal.title} message={modal.message} onCancel={handleCancel} onConfirm={handleConfirm} />
      )}
    </ConfirmContext.Provider>
  )
}

export function useConfirm(){
  return useContext(ConfirmContext)
}
