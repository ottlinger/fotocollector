package de.aikiit.fotocollector;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class ScanEntryTest {

    @Test
    void providesRecordValueSemantics() {
        ScanEntry entry = new ScanEntry("image.jpg", "sha1", new Date(1234), 42L);
        ScanEntry equalEntry = new ScanEntry("image.jpg", "sha1", new Date(1234), 42L);

        assertThat(ScanEntry.class.isRecord()).isTrue();
        assertThat(entry).isEqualTo(equalEntry).hasSameHashCodeAs(equalEntry);
        assertThat(entry.fileName()).isEqualTo("image.jpg");
        assertThat(entry.hashOverContent()).isEqualTo("sha1");
        assertThat(entry.size()).isEqualTo(42L);
    }

    @Test
    void preservesConvenienceConstructorDefaults() {
        ScanEntry entry = new ScanEntry("image.jpg", new Date(1234));

        assertThat(entry.hashOverContent()).isNull();
        assertThat(entry.size()).isZero();
        assertThat(entry.creationDate()).isEqualTo(new Date(1234));
        assertThat(new ScanEntry("image.jpg").creationDate()).isNotNull();
    }

    @Test
    void defensivelyCopiesMutableCreationDate() {
        Date date = new Date(1234);
        ScanEntry entry = new ScanEntry("image.jpg", date);
        int originalHashCode = entry.hashCode();

        date.setTime(5678);
        entry.creationDate().setTime(9999);

        assertThat(entry.creationDate()).isEqualTo(new Date(1234));
        assertThat(entry.hashCode()).isEqualTo(originalHashCode);
    }

    @Test
    void allowsMissingCreationDate() {
        assertThat(new ScanEntry("image.jpg", null).creationDate()).isNull();
    }
}
