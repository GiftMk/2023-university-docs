from tkinter import *

master = Tk()
c = Canvas(master, width=200, height=200)
c.pack()
c.create_line(0, 0, 200, 100, tags=['cool'])
c.create_line(0, 100, 200, 0, fill="red", dash=(4, 4), tags=['cool'])
rect = c.create_rectangle(50, 25, 150, 75, fill="blue")
c.itemconfigure('cool', fill='blue')
c.itemconfigure(rect, fill='red')
c.coords(rect, 10, 10, 50, 100)

master.mainloop()