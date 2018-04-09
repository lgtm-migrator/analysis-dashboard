package edu.hm.hafner.java.persistence;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entity of a Issues object to store in a database.
 *
 * @author Michael Schmid
 */
@Entity
@Table(name = "issues")
public class IssuesEntity {

    /**
     * Set of issue objects related to this issues object.
     */
    @OneToMany
    @OrderColumn
    private List<IssueEntity> elements = new ArrayList<>();

    /**
     * List of info messages stored as a table of strings.
     */
    @ElementCollection(targetClass = String.class)
    @OrderColumn
    private List<String> infoMessages = new ArrayList<>();

    /**
     * List of error messages stored as a table of strings.
     */
    @ElementCollection(targetClass = String.class)
    @OrderColumn
    private List<String> errorMessages = new ArrayList<>();

    private int sizeOfDuplicates = 0;

    @Id
    private String id;

    public List<IssueEntity> getElements() {
        return elements;
    }

    public void setElements(final List<IssueEntity> elements) {
        this.elements = elements;
    }

    public List<String> getInfoMessages() {
        return infoMessages;
    }

    public void setInfoMessages(final List<String> infoMessages) {
        this.infoMessages = infoMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(final List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public int getSizeOfDuplicates() {
        return sizeOfDuplicates;
    }

    public void setSizeOfDuplicates(final int sizeOfDuplicates) {
        this.sizeOfDuplicates = sizeOfDuplicates;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IssuesEntity that = (IssuesEntity) o;
        return sizeOfDuplicates == that.sizeOfDuplicates &&
                Objects.equals(elements, that.elements) &&
                Objects.equals(infoMessages, that.infoMessages) &&
                Objects.equals(errorMessages, that.errorMessages) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(elements, infoMessages, errorMessages, sizeOfDuplicates, id);
    }
}
