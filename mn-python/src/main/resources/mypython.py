import pygal

def bar_chart(numbers, label='Fibonacci'):
  bar_chart = pygal.Bar(height=400)
  bar_chart.add(label, numbers)
  return bar_chart.render(is_unicode=True)

bar_chart