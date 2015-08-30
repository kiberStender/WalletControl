class Total then constructor: (@entrada, @saida, @saldoTotal) ->
  @toString = ->
    saldo = (saldoTotal) -> if saldoTotal > 0 then "<td>R$ #{saldoTotal}</td>" else """<td class="negative">R$ #{saldoTotal}</td>"""
    """<tr></tr><tr>
      <td colspan="2"></td>
      <td>R$ #{parseFloat(@entrada).toFixed 2}</td>
      <td>R$ #{parseFloat(@saida).toFixed 2}</td>
      #{saldo parseFloat(@entrada + @saida).toFixed 2}
    </tr>"""

class TotalEmpty extends Total then constructor: (saldo) -> super(0, 0, saldo)

totalEmpty = (saldo) -> new TotalEmpty saldo

root = exports ? window
root.Total = Total
root.totalEmpty = totalEmpty