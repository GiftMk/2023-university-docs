from tkinter import *
from tkinter.ttk import *  
window = Tk()
frame = Frame(window, height=64, width=64)
frame.pack_propagate(0) # don't shrink
frame.grid(row=0, column=0)
button = Button(frame, text="Hi")
button.pack(fill=BOTH, expand=1)
window.mainloop()