import { renderHook, act } from '@testing-library/react'
import useApi from '../../src/hooks/useApi'
import api from '../../src/services/api'
import { vi, describe, it, expect, beforeEach } from 'vitest'

vi.mock('../../src/services/api')

describe('useApi', () => {
  beforeEach(()=>{
    vi.resetAllMocks()
  })

  it('should set loading and return data on success', async () => {
    api.get.mockResolvedValue({ data: { ok:true } })
    const { result } = renderHook(() => useApi())

    let data
    await act(async () => {
      data = await result.current.get('/test')
    })

    expect(result.current.loading).toBe(false)
    expect(result.current.error).toBe(null)
    expect(data.data).toEqual({ ok:true })
  })

  it('should set error on failure', async () => {
    api.get.mockRejectedValue(new Error('fail'))
    const { result } = renderHook(() => useApi())
    // wrap in act so React state updates (setError) are flushed before assertions
    await act(async () => {
      await expect(result.current.get('/x')).rejects.toThrow('fail')
    })
    expect(result.current.error).toBe('fail')
    expect(result.current.loading).toBe(false)
  })
})