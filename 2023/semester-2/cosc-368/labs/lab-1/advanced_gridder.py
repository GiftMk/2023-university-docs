from tkinter import *
from tkinter.ttk import *

window = Tk()
for label_num in range(6):
  button_text = f"Button {label_num}"
  row = label_num // 2
  col = label_num % 3
  button_text = f"Button {label_num}"
  button = Button(window, text=button_text)
  button.grid(row=row, column=col)
  
  if label_num == 1:
    button.grid(columnspan=2, sticky='ew')
  elif label_num == 3:
    button.grid(rowspan=2, sticky='ns')
  
window.columnconfigure(1, weight=1)
window.rowconfigure(1, weight=1)
window.rowconfigure(2, weight=1)
window.mainloop()


