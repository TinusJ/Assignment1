
import com.mycompany.assignment.entity.Person;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

@Named
@ViewScoped
public class PersonBean implements Serializable {

    private List<Person> persons;

    private Person selectedPerson;

    private List<Person> selectedPersons;

//    @Inject
//    private PersonFacade personFacade;

    @PostConstruct
    public void init() {
        this.persons = new ArrayList<>();//Initialize from facade

        // setup basic data
        Person p1 = new Person(), p2 = new Person(), p3 = new Person(), p4 = new Person(), p5 = new Person(), p6 = new Person(), p7 = new Person();
        p1.setBirthDate(LocalDate.of(1995, Month.MARCH, 8));
        p2.setBirthDate(LocalDate.of(1998, Month.APRIL, 8));
        p3.setBirthDate(LocalDate.of(1992, Month.FEBRUARY, 8));
        p4.setBirthDate(LocalDate.of(2005, Month.MARCH, 22));
        p5.setBirthDate(LocalDate.of(2001, Month.DECEMBER, 8));
        p6.setBirthDate(LocalDate.of(1960, Month.DECEMBER, 20));
        p7.setBirthDate(LocalDate.of(1965, Month.APRIL, 30));

        p1.setId(1L);
        p2.setId(2L);
        p3.setId(3L);
        p4.setId(4L);
        p5.setId(5L);
        p6.setId(6L);
        p7.setId(7L);

        p1.setName("Tinus Jackson");
        p2.setName("Jaun Marie Jackson");
        p3.setName("Kid 1");
        p4.setName("Kid 2");
        p5.setName("Kid 3");
        p6.setName("Parent 1");
        p7.setName("Parent 2");
        
        p1.setParent1(p6);
        p1.setParent2(p7);
        
        p1.setPather(p2);
        p2.setPather(p1);
        
        p1.setChildren(Arrays.asList(p3,p4,p5));
        p2.setChildren(Arrays.asList(p3,p4,p5));

        this.persons.addAll(Arrays.asList(p1, p2, p3, p4,p5,p6));
    }

    public List<Person> getPersons() {
        return persons;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public List<Person> getSelectedPersons() {
        return selectedPersons;
    }

    public void setSelectedPersons(List<Person> selectedPersons) {
        this.selectedPersons = selectedPersons;
    }

    public void openNew() {
        this.selectedPerson = new Person();
    }

    public void savePerson() {
        if (this.selectedPerson.getId() == null) {
            // JPA will cover this with the db connection
            this.selectedPerson.setId(Long.MAX_VALUE - new Random().nextInt());
            this.persons.add(this.selectedPerson);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Person Added"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Person Updated"));
        }

        PrimeFaces.current().executeScript("PF('managePersonDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-persons");
    }

    public void deletePerson() {
        this.persons.remove(this.selectedPerson);
        this.selectedPerson = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Person Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-persons");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedPersons()) {
            int size = this.selectedPersons.size();
            return size > 1 ? size + " persons selected" : "1 product selected";
        }

        return "Delete";
    }

    public boolean hasSelectedPersons() {
        return this.selectedPersons != null && !this.selectedPersons.isEmpty();
    }

    public void deleteSelectedPersons() {
        this.persons.removeAll(this.selectedPersons);
        this.selectedPersons = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Persons Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-persons");
        PrimeFaces.current().executeScript("PF('dtPersons').clearFilters()");
    }

}
