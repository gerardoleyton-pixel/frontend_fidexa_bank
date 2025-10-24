import { renderHook, act } from '@testing-library/react'
import useFormValidation from '../../src/hooks/useFormValidation'
import { describe, it, expect } from 'vitest'

describe('useFormValidation', () => {
  it('validates field rules and validates all', () => {
    const rules = {
      name: v => v ? null : 'required',
      amount: v => (isNaN(Number(v)) || Number(v) <= 0) ? 'invalid' : null
    }
    const { result } = renderHook(() => useFormValidation({ name:'', amount: '' }, rules))

    act(() => {
      result.current.setField('name','John')
      result.current.setField('amount','10')
    })

    expect(result.current.values.name).toBe('John')
    expect(result.current.values.amount).toBe('10')
    expect(result.current.errors.name).toBe(null)
    expect(result.current.errors.amount).toBe(null)
    expect(result.current.validateAll()).toBe(true)

    act(() => { result.current.setField('amount','-1') })
    expect(result.current.errors.amount).toBe('invalid')
    expect(result.current.validateAll()).toBe(false)
  })
})