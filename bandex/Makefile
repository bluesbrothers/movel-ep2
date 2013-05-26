include ../names.mk

alo:
	echo "XD"

$(DOCDIR):
	if [ ! -d $(DOCDIR) ]; then mkdir $(DOCDIR); fi

pdf: $(DOCDIR)
	cd $(DOCDIR); make folderup


.PHONY: clean
clean:
	rm -f *~
	cd $(DOCDIR); make clean
