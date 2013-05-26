MANUALDIR=doc

#
##
# Parte padrao
##
#

alo:
	echo "XD"

$(MANUALDIR):
	if [ ! -d $(MANUALDIR) ]; then mkdir $(MANUALDIR); fi

pdf: $(MANUALDIR)
	cd $(MANUALDIR); make folderup


.PHONY: clean
clean:
	rm -f *~
	cd $(MANUALDIR); make clean
