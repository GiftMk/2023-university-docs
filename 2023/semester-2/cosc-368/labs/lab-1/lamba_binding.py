from tkinter import *
from tkinter.ttk import *

def increment_value(value, n):
  value.set(value.get() + n)

window = Tk()

value = IntVar(window, 0)
label = Label(window, textvariable=value)
label.pack()

button = Button(window, text='Left +1, Right -1')
button.bind('<Button-1>', lambda _: increment_value(value, 1))
button.bind('<Button-2>', lambda _: increment_value(value, -1))
button.pack()

window.mainloop()

