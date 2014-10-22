/**
 * Concrete value types used when interacting with the platform APIs.
 * <p>
 *     Due to the extensible nature of the Jive platform, encountering values which are outside of the common set
 *     will be frequently encountered.  This package provides strongly typed value interfaces for each type as well
 *     as enumerations defining common values for the type interfaces.
 * </p>
 * <p>
 *     To gain the most from these value definitions always consume information using the type interface and produce
 *     values using the enumerations when possible.  When deserializing entity classes, all values which map to
 *     enumeration values will be returned as actual enumeration values.  All other values will be wrapped in a
 *     concrete implementation of the type interface.
 * </p>
 */
package com.jivesoftware.android.mobile.sdk.entity.value;
